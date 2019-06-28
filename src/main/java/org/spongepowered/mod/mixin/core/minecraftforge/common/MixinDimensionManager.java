package org.spongepowered.mod.mixin.core.minecraftforge.common;

import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.common.world.WorldManager;

@Mixin(value = DimensionManager.class, remap = false)
public abstract class MixinDimensionManager implements WorldManager {

}
