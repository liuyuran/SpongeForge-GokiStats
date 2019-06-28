/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.mod.mixin.core.server;

import net.minecraft.profiler.Snooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.bridge.server.MinecraftServerBridge;
import org.spongepowered.common.bridge.server.management.PlayerProfileCacheBridge;
import org.spongepowered.common.bridge.world.ServerWorldBridge;
import org.spongepowered.common.world.WorldManager;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

@Mixin(value = MinecraftServer.class, priority = 1002)
public abstract class MixinMinecraftServer_Forge implements MinecraftServerBridge {

    @Shadow @Final private static Logger LOGGER;
    @Shadow @Final private Snooper usageSnooper;
    @Shadow(remap = false) public Hashtable<Integer, long[]> worldTickTimes;
    @Shadow public abstract PlayerProfileCache getPlayerProfileCache();
    @Shadow public void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String generatorOptions) {
    }

    /**
     * @author Zidane
     * @reason Re-route to {@link WorldManager}.
     */
    @Overwrite
    public WorldServer getWorld(int dimensionId) {
        WorldServer ret = SpongeImpl.getWorldManager().getWorld(dimensionId);
        if (ret == null) {
            ret = SpongeImpl.getWorldManager().loadWorld(dimensionId);
        }

        if (ret == null) {
            ret = SpongeImpl.getWorldManager().getDefaultWorld();
        }

        if (ret == null) {
            throw new RuntimeException("Attempt made to initialize " + dimensionId + " dimension before overworld is loaded!");
        }

        return ret;
    }

    /**
     * @author Zidane
     * @reason Disable async lighting and re-route to {@link WorldManager}.
     */
    @Overwrite
    public void stopServer() {
        LOGGER.info("Stopping server");

        // Sponge Start - Force player profile cache save
        ((PlayerProfileCacheBridge) this.getPlayerProfileCache()).bridge$setCanSave(true);
        ((MinecraftServer) (Object) this).getPlayerProfileCache().save();
        ((PlayerProfileCacheBridge) this.getPlayerProfileCache()).bridge$setCanSave(false);

        final MinecraftServer server = (MinecraftServer) (Object) this;
        if (server.getNetworkSystem() != null) {
            server.getNetworkSystem().terminateEndpoints();
        }

        if (server.getPlayerList() != null) {
            LOGGER.info("Saving players");
            server.getPlayerList().saveAllPlayerData();
            server.getPlayerList().removeAllPlayers();
        }

        if (server.worlds != null) {
            LOGGER.info("Saving worlds");

            for (WorldServer worldserver : server.worlds) {
                if (worldserver != null) {
                    worldserver.disableLevelSaving = false;
                }
            }

            server.saveAllWorlds(false);

            for (WorldServer worldserver1 : server.worlds) {
                if (worldserver1 != null) {
                    // Turn off Async Lighting
                    if (SpongeImpl.getGlobalConfigAdapter().getConfig().getModules().useOptimizations() &&
                        SpongeImpl.getGlobalConfigAdapter().getConfig().getOptimizations().useAsyncLighting()) {
                        ((ServerWorldBridge) worldserver1).bridge$getLightingExecutor().shutdown();

                        try {
                            ((ServerWorldBridge) worldserver1).bridge$getLightingExecutor().awaitTermination(1, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            ((ServerWorldBridge) worldserver1).bridge$getLightingExecutor().shutdownNow();
                        }
                    }

                    SpongeImpl.getWorldManager().unloadWorld(worldserver1);

                    // Sponge End
                    worldserver1.flush();
                }
            }
        }

        if (this.usageSnooper.isSnooperRunning()) {
            this.usageSnooper.stopSnooper();
        }
    }

    @Inject(method = "updateTimeLightAndEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;updateEntities()V", shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onPostUpdateEntities(CallbackInfo ci, Integer ids[], int x, int id, long i, WorldServer worldServer) {
        ServerWorldBridge worldBridge = (ServerWorldBridge) worldServer;
        if (worldBridge.getChunkGCTickInterval() > 0) {
            worldBridge.doChunkGC();
        }
    }

    @Override
    public long[] bridge$getWorldTickTimes(int dimensionId) {
        return this.worldTickTimes.get(dimensionId);
    }

    @Override
    public void bridge$putWorldTickTimes(int dimensionId, long[] tickTimes) {
        this.worldTickTimes.put(dimensionId, tickTimes);
    }

    @Override
    public void bridge$removeWorldTickTimes(int dimensionId) {
        this.worldTickTimes.remove(dimensionId);
    }
}
