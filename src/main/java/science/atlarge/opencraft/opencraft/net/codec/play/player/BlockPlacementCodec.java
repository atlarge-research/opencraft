package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.player.BlockPlacementMessage;
import org.bukkit.util.BlockVector;

public final class BlockPlacementCodec implements Codec<BlockPlacementMessage> {

    @Override
    public BlockPlacementMessage decode(ByteBuf buffer) throws IOException {
        BlockVector pos = GlowBufUtils.readBlockPosition(buffer);
        int direction = buffer.readByte();
        int hand = ByteBufUtils.readVarInt(buffer);
        float cursorX = buffer.readFloat();
        float cursorY = buffer.readFloat();
        float cursorZ = buffer.readFloat();
        return new BlockPlacementMessage(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(),
            direction, hand, cursorX, cursorY, cursorZ);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, BlockPlacementMessage message) {
        GlowBufUtils.writeBlockPosition(buffer, message.getX(), message.getY(), message.getZ());
        buffer.writeByte(message.getDirection());
        ByteBufUtils.writeVarInt(buffer, message.getHand());
        buffer.writeFloat(message.getCursorX());
        buffer.writeFloat(message.getCursorY());
        buffer.writeFloat(message.getCursorZ());
        return buffer;
    }
}
