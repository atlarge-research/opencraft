package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityTeleportMessage;

public final class EntityTeleportCodec implements Codec<EntityTeleportMessage> {

    @Override
    public EntityTeleportMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        int rotation = buffer.readByte();
        int pitch = buffer.readByte();
        boolean ground = buffer.readBoolean();
        return new EntityTeleportMessage(id, x, y, z, rotation, pitch, ground);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, EntityTeleportMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        buffer.writeDouble(message.getX());
        buffer.writeDouble(message.getY());
        buffer.writeDouble(message.getZ());
        buffer.writeByte(message.getRotation());
        buffer.writeByte(message.getPitch());
        buffer.writeBoolean(message.isOnGround());
        return buffer;
    }
}
