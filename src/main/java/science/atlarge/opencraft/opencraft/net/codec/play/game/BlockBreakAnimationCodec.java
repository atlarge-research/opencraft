package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.game.BlockBreakAnimationMessage;
import org.bukkit.util.BlockVector;

public class BlockBreakAnimationCodec implements Codec<BlockBreakAnimationMessage> {

    @Override
    public BlockBreakAnimationMessage decode(ByteBuf buffer) throws IOException {
        int entityId = ByteBufUtils.readVarInt(buffer);
        BlockVector vector = GlowBufUtils.readBlockPosition(buffer);
        int destroyStage = buffer.readByte();
        return new BlockBreakAnimationMessage(
                entityId,
                vector.getBlockX(),
                vector.getBlockY(),
                vector.getBlockZ(),
                destroyStage
        );
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, BlockBreakAnimationMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        GlowBufUtils.writeBlockPosition(buffer, message.getX(), message.getY(), message.getZ());
        buffer.writeByte(message.getDestroyStage());
        return buffer;
    }
}
