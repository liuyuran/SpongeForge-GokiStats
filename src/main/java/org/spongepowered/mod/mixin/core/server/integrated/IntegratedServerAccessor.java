package org.spongepowered.mod.mixin.core.server.integrated;

import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SideOnly(Side.CLIENT)
@Mixin(IntegratedServer.class)
public interface IntegratedServerAccessor {

    @Accessor("worldSettings") WorldSettings getWorldSettings();
}
