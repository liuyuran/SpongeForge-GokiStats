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
package net.infstudio.goki.client.gui;

import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.infstudio.goki.common.stats.StatBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class GuiStatButton extends GuiButton {
    public StatBase stat;
    public EntityPlayer player;

    public GuiStatButton(int id, int x, int y, int width, int height, StatBase stat, EntityPlayer player) {
        super(id, x, y, width, height, "");
        this.stat = stat;
        this.player = player;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            int iconY = 24 * (this.stat.imageID % 10);
            int level = DataHelper.getPlayerStatLevel(this.player, this.stat);
            int cost = this.stat.getCost(level);
            int playerXP = DataHelper.getXPTotal(this.player);

            FontRenderer fontrenderer = mc.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = isUnderMouse(mouseX, mouseY);
            int which = getHoverState(this.hovered);

            int iconX = 0;
            if (which == 2) {
                iconX = 24;
            }
            if (playerXP < cost) {
                iconX = 48;
            }
            if (level >= this.stat.getLimit()) {
                iconX = 72;
            }
            if (!DataHelper.canPlayerRevertStat(player, this.stat) && GuiScreen.isCtrlKeyDown()) {
                iconX = 48;
            }

            String message = level + "";
            if (!this.stat.enabled) {
                iconX = 48;
                message = "X";
            }

            int messageColor = 16777215;
            if (level >= this.stat.getLimit()) {
                message = "*" + level + "*";
                messageColor = 16763904;
            }

            iconX += this.stat.imageID % 20 / 10 * 24 * 4;

            if (this.stat.imageID >= 20) {
                mc.getTextureManager().bindTexture(Reference.RPG_ICON_2_TEXTURE_LOCATION);
            } else {
                mc.getTextureManager().bindTexture(Reference.RPG_ICON_TEXTURE_LOCATION);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(this.x, this.y, 0.0F);
            GL11.glScalef(GuiStats.SCALE, GuiStats.SCALE, 0.0F);
            drawTexturedModalRect(0, 0, iconX, iconY, this.width, this.height);

            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(this.x, this.y, 0.0F);
            drawCenteredString(fontrenderer,
                    message,
                    (int) (this.width / 2 * GuiStats.SCALE),
                    (int) (this.height * GuiStats.SCALE) + 2,
                    messageColor);
            GL11.glPopMatrix();
        }
    }

    public boolean isUnderMouse(int mouseX, int mouseY) {
        return (mouseX >= this.x) && (mouseY >= this.y) && (mouseX < this.x + this.width * GuiStats.SCALE) && (mouseY < this.y + this.height * GuiStats.SCALE);
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int mouseX, int mouseY) {
        return (this.enabled) && (this.visible) && (mouseX >= this.x) && (mouseY >= this.y) && (mouseX < this.x + this.width * GuiStats.SCALE) && (mouseY < this.y + this.height * GuiStats.SCALE);
    }

    public String getHoverMessage(int which) {
        if (which == 0) {
            return this.stat.getLocalizedName() + " L" + DataHelper.getPlayerStatLevel(this.player,
                    this.stat);
        }

        return this.stat.getLocalizedDes(this.player);
    }
}