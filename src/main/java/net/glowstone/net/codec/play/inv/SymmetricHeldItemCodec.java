package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.inv.HeldItemMessage;

public final class SymmetricHeldItemCodec implements Codec<HeldItemMessage> {

    @Override
    public HeldItemMessage decode(ByteBuf buffer) {
        int slot = buffer.readByte();
        return new HeldItemMessage(slot);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, HeldItemMessage message) {
        buffer.writeByte(message.getSlot());
        return buffer;
    }
}
