package org.spongepowered.mod.mixin.core.server.integrated;

import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.common.bridge.server.integrated.IntegratedServerBridge;
import org.spongepowered.mod.mixin.core.server.MixinMinecraftServer_Forge;

@SideOnly(Side.CLIENT)
@Mixin(IntegratedServer.class)
public abstract class MixinIntegratedServer_Forge extends MixinMinecraftServer_Forge implements IntegratedServerBridge {
    private boolean isNewSave;

    /**
     * @author bloodmc
     * @reason In order to guarantee that both client and server load worlds the
     * same using our custom logic, we call super and handle any client specific
     * cases there.
     */
    @Overwrite
    public void loadAllWorlds(String overworldFolder, String unused, long seed, WorldType type, String generator) {
        super.loadAllWorlds(overworldFolder, unused, seed, type, generator);
    }

    @Override
    public void bridge$markNewSave() {
        this.isNewSave = true;
    }

    @Override
    public boolean bridge$isNewSave() {
        return this.isNewSave;
    }
}
