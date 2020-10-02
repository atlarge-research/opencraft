package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.entity.EntityRotationMessage;

public final class EntityRotationCodec implements Codec<EntityRotationMessage> {

    @Override
    public EntityRotationMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        int rotation = buffer.readByte();
        int pitch = buffer.readByte();
        boolean ground = buffer.readBoolean();
        return new EntityRotationMessage(id, rotation, pitch, ground);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, EntityRotationMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        buffer.writeByte(message.getRotation());
        buffer.writeByte(message.getPitch());
        buffer.writeBoolean(message.isOnGround());
        return buffer;
    }
}
