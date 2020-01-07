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
package net.infstudio.goki.common;

import net.infstudio.goki.common.config.ConfigManager;
import net.infstudio.goki.common.config.Configurable;
import net.infstudio.goki.common.stats.StatBase;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;

public class StatsCommand extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "reloadGokiStats";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/gokistats reload";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender icommandsender, String[] astring) {
        StatBase.stats.forEach(Configurable::reloadConfig);
        ConfigManager.INSTANCE.reloadConfig();
        EntityPlayer player;
        if ((icommandsender instanceof EntityPlayer)) {
            player = (EntityPlayer) icommandsender;
            player.sendMessage(new TextComponentTranslation("Reloaded gokistats configuration file."));
        } else {
            server.logInfo("Reloaded gokistats configuration file.");
        }
    }

    public String getCommandUsage(ICommandSender icommandsender) {
        return null;
    }

}