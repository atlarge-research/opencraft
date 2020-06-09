package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.entity.SpawnLightningStrikeMessage;

public final class SpawnLightningStrikeCodec implements Codec<SpawnLightningStrikeMessage> {

    @Override
    public SpawnLightningStrikeMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        int mode = buffer.readByte();
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        return new SpawnLightningStrikeMessage(id, mode, x, y, z);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SpawnLightningStrikeMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        buffer.writeByte(message.getMode());
        buffer.writeDouble(message.getX());
        buffer.writeDouble(message.getY());
        buffer.writeDouble(message.getZ());
        return buffer;
    }
}
