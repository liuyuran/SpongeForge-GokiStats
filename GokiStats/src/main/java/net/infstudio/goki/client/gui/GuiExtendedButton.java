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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class GuiExtendedButton extends GuiButton {
    private int backgroundColor;
    private int borderColor = -16777216;
    private boolean pressed = false;

    public GuiExtendedButton(int id, int x, int y, int width, int height, String text, int color) {
        super(id, x, y, width, height, text);
        this.backgroundColor = color;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.enabled) {
            if (!isUnderMouse(mouseX, mouseY)) {
                drawIdle(mc, mouseX, mouseY);
            } else if (this.pressed) {
                drawDown(mc, mouseX, mouseY);
            } else {
                drawHover(mc, mouseX, mouseY);
            }
        } else
            drawDisabled(mc, mouseX, mouseY);
    }

    public boolean isUnderMouse(int mouseX, int mouseY) {
        return (mouseX >= this.x) && (mouseY >= this.y) && (mouseX < this.x + this.width) && (mouseY < this.y + this.height);
    }

    private void drawBorder() {
        drawHorizontalLine(-1, this.width, 0, this.borderColor);
        drawHorizontalLine(-1, this.width, this.height, this.borderColor);
        drawVerticalLine(-1, 0, this.height, this.borderColor);
        drawVerticalLine(this.width, 0, this.height, this.borderColor);
    }

    private void drawDisabled(Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        Gui.drawRect(0, 0, this.width, this.height, -2011028958);
        drawCenteredString(mc.fontRenderer,
                this.displayString,
                this.width / 2,
                this.height / 2 - mc.fontRenderer.FONT_HEIGHT / 2 + 1,
                3355443);
        drawBorder();
        GL11.glPopMatrix();
    }

    private void drawIdle(Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        Gui.drawRect(0,
                0,
                this.width,
                this.height,
                this.backgroundColor + -2013265920);
        drawCenteredString(mc.fontRenderer,
                this.displayString,
                this.width / 2,
                this.height / 2 - mc.fontRenderer.FONT_HEIGHT / 2 + 1,
                16777215);
        drawBorder();
        GL11.glPopMatrix();
    }

    private void drawHover(Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        Gui.drawRect(0,
                0,
                this.width,
                this.height,
                this.backgroundColor + -16777216);
        drawCenteredString(mc.fontRenderer,
                this.displayString,
                this.width / 2,
                this.height / 2 - mc.fontRenderer.FONT_HEIGHT / 2 + 1,
                16763904);
        drawBorder();
        GL11.glPopMatrix();
    }

    private void drawDown(Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        Gui.drawRect(0, 0, this.width, this.height, -16777216);
        drawCenteredString(mc.fontRenderer,
                this.displayString,
                this.width / 2,
                this.height / 2 - mc.fontRenderer.FONT_HEIGHT / 2 + 1,
                16763904);
        drawBorder();
        GL11.glPopMatrix();
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    public void onPressed() {
        this.pressed = true;
    }

    public void onReleased() {
        this.pressed = false;
    }

    public boolean isPressed() {
        return this.pressed;
    }
}