package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import net.glowstone.entity.meta.MetadataMap.Entry;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.net.message.play.entity.SpawnMobMessage;

public final class SpawnMobCodec implements Codec<SpawnMobMessage> {

    @Override
    public SpawnMobMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        UUID uuid = GlowBufUtils.readUuid(buffer);
        int type = ByteBufUtils.readVarInt(buffer);
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        int headPitch = buffer.readByte();
        int pitch = buffer.readByte();
        int rotation = buffer.readByte();
        int velX = buffer.readShort();
        int velY = buffer.readShort();
        int velZ = buffer.readShort();
        List<Entry> list = GlowBufUtils.readMetadata(buffer);
        return new SpawnMobMessage(id, uuid, type, x, y, z, rotation, pitch, headPitch, velX, velY, velZ, list);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SpawnMobMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        GlowBufUtils.writeUuid(buffer, message.getUuid());
        ByteBufUtils.writeVarInt(buffer, message.getType());
        buffer.writeDouble(message.getX());
        buffer.writeDouble(message.getY());
        buffer.writeDouble(message.getZ());
        buffer.writeByte(message.getHeadPitch());
        buffer.writeByte(message.getPitch());
        buffer.writeByte(message.getRotation());
        buffer.writeShort(message.getVelX());
        buffer.writeShort(message.getVelY());
        buffer.writeShort(message.getVelZ());
        GlowBufUtils.writeMetadata(buffer, message.getMetadata());
        return buffer;
    }
}
