package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.net.message.play.game.PlayEffectMessage;
import org.bukkit.util.BlockVector;

public final class PlayEffectCodec implements Codec<PlayEffectMessage> {

    @Override
    public PlayEffectMessage decode(ByteBuf buffer) throws IOException {
        int id = buffer.readInt();
        BlockVector position = GlowBufUtils.readBlockPosition(buffer);
        int x = position.getBlockX();
        int y = position.getBlockY();
        int z = position.getBlockZ();
        int data = buffer.readInt();
        boolean ignoreDistance = buffer.readBoolean();
        return new PlayEffectMessage(id, x, y, z, data, ignoreDistance);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, PlayEffectMessage message) throws IOException {
        buffer.writeInt(message.getId());
        GlowBufUtils.writeBlockPosition(buffer, message.getX(), message.getY(), message.getZ());
        buffer.writeInt(message.getData());
        buffer.writeBoolean(message.isIgnoreDistance());
        return buffer;
    }
}
