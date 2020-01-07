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
package net.infstudio.goki.common.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.stats.StatBase;
import net.minecraft.entity.player.EntityPlayer;

public class PacketStatSync implements GokiPacket {
    private int[] statLevels;
    private int[] revertedStatLevels;

    public PacketStatSync() {
    }

    public PacketStatSync(EntityPlayer player) {
        this.statLevels = new int[StatBase.stats.size()];
        this.revertedStatLevels = new int[StatBase.stats.size()];
        for (int i = 0; i < this.statLevels.length; i++) {
            if (StatBase.stats.get(i) != null) {
                this.statLevels[i] = DataHelper.getPlayerStatLevel(player,
                        StatBase.stats.get(i));
                this.revertedStatLevels[i] = DataHelper.getPlayerRevertStatLevel(player, StatBase.stats.get(i));
            }
        }
    }

    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        for (int statLevel : this.statLevels) {
            buffer.writeInt(statLevel);
        }
        for (int revertedStatLevel: this.revertedStatLevels) {
            buffer.writeInt(revertedStatLevel);
        }
    }

    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.statLevels = new int[StatBase.stats.size()];
        this.revertedStatLevels = new int[StatBase.stats.size()];
        for (int i = 0; i < this.statLevels.length; i++) {
            this.statLevels[i] = buffer.readInt();
        }
        for (int i = 0; i < this.revertedStatLevels.length; i++) {
            this.revertedStatLevels[i] = buffer.readInt();
        }
    }

    public void handleClientSide(EntityPlayer player) {
        for (int i = 0; i < this.statLevels.length; i++) {
            DataHelper.setPlayerStatLevel(player,
                    StatBase.stats.get(i),
                    this.statLevels[i]);
        }
        for (int i = 0; i < this.revertedStatLevels.length; i++) {
            DataHelper.setPlayerRevertStatLevel(player, StatBase.stats.get(i), this.revertedStatLevels[i]);
        }
    }

    public void handleServerSide(EntityPlayer player) {
    }
}