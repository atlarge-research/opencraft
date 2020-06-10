package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.entity.SpawnXpOrbMessage;

public final class SpawnXpOrbCodec implements Codec<SpawnXpOrbMessage> {

    @Override
    public SpawnXpOrbMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        short count = buffer.readShort();
        return new SpawnXpOrbMessage(id, x, y, z, count);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SpawnXpOrbMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        buffer.writeDouble(message.getX());
        buffer.writeDouble(message.getY());
        buffer.writeDouble(message.getZ());
        buffer.writeShort(message.getCount());
        return buffer;
    }
}
