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
package net.infstudio.goki.common.stats.damage;

import net.infstudio.goki.common.config.stats.DamageSourceProtectionConfig;
import net.infstudio.goki.common.stats.StatBase;
import net.minecraft.util.DamageSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DamageSourceProtectionStat extends StatBase<DamageSourceProtectionConfig> {
    public List<String> damageSources = new ArrayList<>();

    public DamageSourceProtectionStat(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public DamageSourceProtectionConfig createConfig() {
        DamageSourceProtectionConfig config = new DamageSourceProtectionConfig();
        Collections.addAll(config.damageSources, getDefaultDamageSources());
        return config;
    }

    @Override
    public void save() {
        super.save();
        getConfig().damageSources.clear();
        getConfig().damageSources.addAll(damageSources);
    }

    @Override
    public void reload() {
        super.reload();
        damageSources.clear();
        damageSources.addAll(getConfig().damageSources);
    }

    @Override
    public boolean needAffectedByStat(Object... obj) {
        if (obj != null) {
            if ((obj[0] instanceof DamageSource)) {
                DamageSource source = (DamageSource) obj[0];
                for (String damageSource : this.damageSources) {
                    if (source.damageType.equals(damageSource)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public abstract String[] getDefaultDamageSources();
}