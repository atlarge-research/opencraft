package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.inv.CloseWindowMessage;

public final class CloseWindowCodec implements Codec<CloseWindowMessage> {

    @Override
    public CloseWindowMessage decode(ByteBuf buffer) {
        int id = buffer.readUnsignedByte();
        return new CloseWindowMessage(id);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, CloseWindowMessage message) {
        buffer.writeByte(message.getId());
        return buffer;
    }
}
