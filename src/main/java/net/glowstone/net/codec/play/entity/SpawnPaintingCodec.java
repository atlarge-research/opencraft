package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.UUID;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.net.message.play.entity.SpawnPaintingMessage;
import org.bukkit.util.BlockVector;

public final class SpawnPaintingCodec implements Codec<SpawnPaintingMessage> {

    @Override
    public SpawnPaintingMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        UUID uuid = GlowBufUtils.readUuid(buffer);
        String title = ByteBufUtils.readUTF8(buffer);
        BlockVector vector = GlowBufUtils.readBlockPosition(buffer);
        int facing = buffer.readByte();
        return new SpawnPaintingMessage(
            id,
            uuid,
            title,
            vector.getBlockX(),
            vector.getBlockY(),
            vector.getBlockZ(),
            facing
        );
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SpawnPaintingMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        GlowBufUtils.writeUuid(buffer, message.getUniqueId());
        ByteBufUtils.writeUTF8(buffer, message.getTitle());
        GlowBufUtils.writeBlockPosition(buffer, message.getX(), message.getY(), message.getZ());
        buffer.writeByte(message.getFacing());
        return buffer;
    }
}
