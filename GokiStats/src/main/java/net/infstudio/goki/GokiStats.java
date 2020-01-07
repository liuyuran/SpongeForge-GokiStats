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
package net.infstudio.goki;

import net.infstudio.goki.common.CommonProxy;
import net.infstudio.goki.common.StatsCommand;
import net.infstudio.goki.common.config.ConfigManager;
import net.infstudio.goki.common.config.Configurable;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.init.MinecraftEffects;
import net.infstudio.goki.common.network.packet.*;
import net.infstudio.goki.common.stats.StatBase;
import net.infstudio.goki.common.stats.Stats;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import java.io.IOException;
import java.nio.file.Files;

@Mod(modid = Reference.MODID, useMetadata = true, updateJSON = "https://infinitystudio.github.io/Updates/gokistats.json")
public class GokiStats {
    public static final PacketPipeline packetPipeline = new PacketPipeline();

    @Mod.Instance(Reference.MODID)
    public static GokiStats instance;

    @SidedProxy(clientSide = "net.infstudio.goki.client.ClientProxy", serverSide = "net.infstudio.goki.common.CommonProxy")
    public static CommonProxy proxy;

    private static Class<?>[] loadClasses = {
            Stats.class, MinecraftEffects.class
    };

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        try {
            for (Class<?> clz : loadClasses)
                Class.forName(clz.getName());
        } catch (ClassNotFoundException ignored) {
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;

        if (GokiConfig.version.equals("v2")) { // Skip v2
            try {
                Files.deleteIfExists(event.getModConfigurationDirectory().toPath().resolve(Reference.MODID + "_v2.cfg"));
                Files.list(event.getModConfigurationDirectory().toPath().resolve(Reference.MODID)).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                Files.deleteIfExists(event.getModConfigurationDirectory().toPath().resolve(Reference.MODID));
                net.minecraftforge.common.config.ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        new ConfigManager(event.getModConfigurationDirectory().toPath().resolve(Reference.MODID));
        System.out.println(StatBase.totalStats);
        StatBase.stats.forEach(Configurable::reloadConfig);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        packetPipeline.initialise();
        packetPipeline.registerPacket(PacketStatSync.class);
        packetPipeline.registerPacket(PacketStatAlter.Up.class);
        packetPipeline.registerPacket(PacketStatAlter.Down.class);
        packetPipeline.registerPacket(PacketSyncXP.class);
        packetPipeline.registerPacket(PacketSyncStatConfig.class);

        proxy.registerHandlers();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        packetPipeline.postInitialise();
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        StatBase.stats.forEach(StatBase::reloadConfig);
        ConfigManager.INSTANCE.reloadConfig();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        ICommandManager command = server.getCommandManager();
        ServerCommandManager serverCommand = (ServerCommandManager) command;
        serverCommand.registerCommand(new StatsCommand());
        // TODO notice it's a reversion
    }
}