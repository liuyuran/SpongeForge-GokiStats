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
package net.infstudio.goki.common.utils;

import net.minecraft.util.ResourceLocation;

public class Reference {
    public static final String MODID = "gokistats";
    public static final String MOD_NAME = "gokistats";
    public static final ResourceLocation RPG_ICON_TEXTURE_LOCATION = new ResourceLocation("gokistats".toLowerCase(), "textures/rpg_icons.png");
    public static final ResourceLocation RPG_ICON_2_TEXTURE_LOCATION = new ResourceLocation("gokistats".toLowerCase(), "textures/rpg_icons_2.png");
    public static final ResourceLocation PARTICLES_TEXTURE = new ResourceLocation("gokistats".toLowerCase(), "textures/particles.png");
    public static final String STAT_TAG = "gokistats_Stats";
    public static boolean isPlayerAPILoaded = false;
}