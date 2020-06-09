package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.entity.AttachEntityMessage;

public final class AttachEntityCodec implements Codec<AttachEntityMessage> {

    @Override
    public AttachEntityMessage decode(ByteBuf buffer) {
        int attached = buffer.readInt();
        int holding = buffer.readInt();
        return new AttachEntityMessage(attached, holding);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, AttachEntityMessage message) {
        buffer.writeInt(message.getAttached());
        buffer.writeInt(message.getHolding());
        return buffer;
    }
}
