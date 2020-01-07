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
import net.minecraft.entity.player.EntityPlayer;

public class PacketSyncXP implements GokiPacket {
    private float experience;
    private int experienceLevel;
    private int experienceTotal;

    public PacketSyncXP() {
    }

    PacketSyncXP(EntityPlayer player) {
        this.experience = player.experience;
        this.experienceLevel = player.experienceLevel;
        this.experienceTotal = player.experienceTotal;
    }

    public PacketSyncXP(float experience, int experienceLevel, int experienceTotal) {
        this.experience = experience;
        this.experienceLevel = experienceLevel;
        this.experienceTotal = experienceTotal;
    }

    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeFloat(this.experience);
        buffer.writeInt(this.experienceLevel);
        buffer.writeInt(this.experienceTotal);
    }

    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.experience = buffer.readFloat();
        this.experienceLevel = buffer.readInt();
        this.experienceTotal = buffer.readInt();
    }

    public void handleClientSide(EntityPlayer player) {
        player.experience = this.experience;
        player.experienceLevel = this.experienceLevel;
        player.experienceTotal = this.experienceTotal;
    }

    public void handleServerSide(EntityPlayer player) {
        player.experience = this.experience;
        player.experienceLevel = this.experienceLevel;
        player.experienceTotal = this.experienceTotal;
    }
}