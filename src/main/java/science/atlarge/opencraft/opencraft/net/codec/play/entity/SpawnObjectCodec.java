package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.UUID;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnObjectMessage;

public final class SpawnObjectCodec implements Codec<SpawnObjectMessage> {

    @Override
    public SpawnObjectMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        UUID uuid = GlowBufUtils.readUuid(buffer);
        int type = buffer.readByte();
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        int pitch = buffer.readByte();
        int yaw = buffer.readByte();
        int data = buffer.readInt();
        int velX = buffer.readShort();
        int velY = buffer.readShort();
        int velZ = buffer.readShort();
        return new SpawnObjectMessage(id, uuid, type, x, y, z, pitch, yaw, data, velX, velY, velZ);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SpawnObjectMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        GlowBufUtils.writeUuid(buffer, message.getUuid());
        buffer.writeByte(message.getType());
        buffer.writeDouble(message.getX());
        buffer.writeDouble(message.getY());
        buffer.writeDouble(message.getZ());
        buffer.writeByte(message.getPitch());
        buffer.writeByte(message.getYaw());
        buffer.writeInt(message.getData());
        buffer.writeShort(message.getVelX());
        buffer.writeShort(message.getVelY());
        buffer.writeShort(message.getVelZ());
        return buffer;
    }
}
