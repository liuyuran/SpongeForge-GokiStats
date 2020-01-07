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
package net.infstudio.goki.common.config;

import java.lang.reflect.Type;

public interface Configurable<T> {
    T createConfig();

    default void saveConfig() {
        save();
        ConfigManager.INSTANCE.getConfigMap().put(getKey(), getConfig());
        ConfigManager.INSTANCE.saveConfig(getKey());
    }

    default Type getType() {
        return createConfig().getClass();
    }

    default void reloadConfig() {
        ConfigManager.INSTANCE.registerConfig(getKey(), getType());
        if (!ConfigManager.INSTANCE.hasConfig(getKey()))
            ConfigManager.INSTANCE.createConfig(getKey(), createConfig());
        ConfigManager.INSTANCE.reloadConfig(getKey());
        reload();
    }

    void save();

    void reload();

    String getKey();

    default T getConfig() {
        return ConfigManager.INSTANCE.getOrCreateConfig(getKey(), createConfig());
    }
}
