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

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.Objects;

public class IDMDTuple {
    public String id;
    public int metadata;

    public IDMDTuple(Block block, int bMD) {
        this.id = block.getRegistryName().toString();
        this.metadata = bMD;
    }

    public IDMDTuple(Item item, int bMD) {
        this.id = item.getRegistryName().toString();
        this.metadata = bMD;
    }

    public boolean equals(Object object) {
        if ((object instanceof IDMDTuple)) {
            IDMDTuple entry = (IDMDTuple) object;
            if ((Objects.equals(entry.id, this.id)) && (entry.metadata == this.metadata)) {
                return true;
            }
        }
        return super.equals(object);
    }
}