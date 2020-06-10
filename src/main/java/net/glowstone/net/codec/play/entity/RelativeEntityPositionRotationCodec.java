package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.entity.RelativeEntityPositionRotationMessage;

public final class RelativeEntityPositionRotationCodec implements
    Codec<RelativeEntityPositionRotationMessage> {

    @Override
    public RelativeEntityPositionRotationMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        short x = buffer.readShort();
        short y = buffer.readShort();
        short z = buffer.readShort();
        int rotation = buffer.readByte();
        int pitch = buffer.readByte();
        boolean ground = buffer.readBoolean();
        return new RelativeEntityPositionRotationMessage(id, x, y, z, rotation, pitch, ground);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, RelativeEntityPositionRotationMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        buffer.writeShort(message.getDeltaX());
        buffer.writeShort(message.getDeltaY());
        buffer.writeShort(message.getDeltaZ());
        buffer.writeByte(message.getRotation());
        buffer.writeByte(message.getPitch());
        buffer.writeBoolean(message.isOnGround());
        return buffer;
    }
}
