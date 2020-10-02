package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.game.SpawnPositionMessage;
import org.bukkit.util.BlockVector;

public final class SpawnPositionCodec implements Codec<SpawnPositionMessage> {

    @Override
    public SpawnPositionMessage decode(ByteBuf buffer) {
        BlockVector pos = GlowBufUtils.readBlockPosition(buffer);
        return new SpawnPositionMessage(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SpawnPositionMessage message) {
        GlowBufUtils.writeBlockPosition(buffer, message.getX(), message.getY(), message.getZ());
        return buffer;
    }
}
