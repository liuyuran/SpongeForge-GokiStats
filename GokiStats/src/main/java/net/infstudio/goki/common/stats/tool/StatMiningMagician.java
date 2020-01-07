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
package net.infstudio.goki.common.stats.tool;

import net.infstudio.goki.common.config.stats.MiningMagicianConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.stats.StatBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatMiningMagician extends StatBase<MiningMagicianConfig> {
    public static List<IDMDTuple> blockEntries = new ArrayList<>();
    public static List<IDMDTuple> itemEntries = new ArrayList<>();
    private static IDMDTuple[] defaultBlockEntries =
            {new IDMDTuple(Blocks.COAL_ORE, 0), new IDMDTuple(Blocks.DIAMOND_ORE, 0), new IDMDTuple(Blocks.EMERALD_ORE, 0), new IDMDTuple(Blocks.GOLD_ORE, 0), new IDMDTuple(Blocks.IRON_ORE, 0), new IDMDTuple(Blocks.LAPIS_ORE, 0), new IDMDTuple(Blocks.QUARTZ_ORE, 0), new IDMDTuple(Blocks.REDSTONE_ORE, 0)};
    private static IDMDTuple[] defaultItemEntries =
            {new IDMDTuple(Items.COAL, 0), new IDMDTuple(Items.DIAMOND, 0), new IDMDTuple(Items.EMERALD, 0), new IDMDTuple(Items.GOLD_INGOT, 0), new IDMDTuple(Items.IRON_INGOT, 0), new IDMDTuple(Items.DYE, 4), new IDMDTuple(Items.QUARTZ, 0), new IDMDTuple(Items.REDSTONE, 0)};

    public StatMiningMagician(int id, String key, int limit) {
        super(id, key, limit);
        Collections.addAll(blockEntries, defaultBlockEntries);
        Collections.addAll(itemEntries, defaultItemEntries);
    }

    @Override
    public void save() {
        super.save();
        getConfig().blockEntries.clear();
        getConfig().blockEntries.addAll(blockEntries);
        getConfig().itemEntries.clear();
        getConfig().itemEntries.addAll(itemEntries);
    }

    @Override
    public MiningMagicianConfig createConfig() {
        MiningMagicianConfig config = new MiningMagicianConfig();
        Collections.addAll(config.blockEntries, defaultBlockEntries);
        Collections.addAll(config.itemEntries, defaultItemEntries);
        return config;
    }

    @Override
    public void reload() {
        super.reload();
        blockEntries.clear();
        itemEntries.clear();
        blockEntries.addAll(getConfig().blockEntries);
        itemEntries.addAll(getConfig().itemEntries);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.3F);
    }

    @Override
    public float[] getAppliedDescriptionVar(EntityPlayer player) {
        return new float[]
                {DataHelper.trimDecimals(getBonus(player), 1)};
    }

    @Override
    public boolean needAffectedByStat(Object... obj) {
        IDMDTuple idmd;
        if ((obj[0] instanceof IDMDTuple)) {
            idmd = (IDMDTuple) obj[0];
            for (IDMDTuple entry : blockEntries) {
                if (idmd.equals(entry)) {
                    return true;
                }
            }
            for (IDMDTuple entry : itemEntries) {
                if (idmd.equals(entry)) {
                    return true;
                }
            }
        }
        return false;
        // return super.needAffectedByStat(obj[0]);
    }
}