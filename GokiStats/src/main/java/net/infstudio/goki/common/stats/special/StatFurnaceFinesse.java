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
package net.infstudio.goki.common.stats.special;

import net.infstudio.goki.common.stats.StatSpecial;
import net.infstudio.goki.common.stats.StatSpecialBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.resources.I18n;

public class StatFurnaceFinesse extends StatSpecialBase implements StatSpecial {
    public StatFurnaceFinesse(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return Math.min(getFinalBonus(level / 5.0F), 199.0F) + 1.0F;
    }

    @Override
    public float getSecondaryBonus(int level) {
        return Math.min(getFinalBonus(level), 100.0F);
    }

    @Override
    public float[] getAppliedDescriptionVar(EntityPlayer player) {
        // TODO special
        int level = getPlayerStatLevel(player);
        return new float[]
                {getBonus(level), getSecondaryBonus(level)};
        // return "Smelt " + getBonus(level) + " ticks faster " +
        // getSecondaryBonus(level) + "% of the time while using a furnace.";
    }

    @Override
    public String getLocalizedDes(EntityPlayer player) {
        return I18n.format(this.key + ".des",
                this.getAppliedDescriptionVar(player)[0],
                this.getAppliedDescriptionVar(player)[1]);
    }
}