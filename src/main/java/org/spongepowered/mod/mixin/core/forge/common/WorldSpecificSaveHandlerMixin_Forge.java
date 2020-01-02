package org.spongepowered.mod.mixin.core.forge.common;

import net.minecraftforge.common.WorldSpecificSaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.world.WorldManager;

import java.io.File;

@Mixin(WorldSpecificSaveHandler.class)
public abstract class WorldSpecificSaveHandlerMixin_Forge {

    @Shadow private File dataDir;

    @Redirect(method = "getMapFileFromName", at = @At(value = "INVOKE", target = "Ljava/io/File;mkdirs()Z", remap = false))
    private boolean sponge$onCreateDataDirectory(File dir) {
        return WorldManager.mkdirsIfSaveable(dir);
    }
}
