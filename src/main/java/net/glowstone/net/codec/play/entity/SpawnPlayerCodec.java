package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import net.glowstone.entity.meta.MetadataMap.Entry;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.net.message.play.entity.SpawnPlayerMessage;

public final class SpawnPlayerCodec implements Codec<SpawnPlayerMessage> {

    @Override
    public SpawnPlayerMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        UUID uuid = GlowBufUtils.readUuid(buffer);
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        int rotation = buffer.readByte();
        int pitch = buffer.readByte();
        List<Entry> list = GlowBufUtils.readMetadata(buffer);
        return new SpawnPlayerMessage(id, uuid, x, y, z, rotation, pitch, list);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SpawnPlayerMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        GlowBufUtils.writeUuid(buffer, message.getUuid());
        buffer.writeDouble(message.getX());
        buffer.writeDouble(message.getY());
        buffer.writeDouble(message.getZ());
        buffer.writeByte(message.getRotation());
        buffer.writeByte(message.getPitch());
        GlowBufUtils.writeMetadata(buffer, message.getMetadata());
        return buffer;
    }
}
