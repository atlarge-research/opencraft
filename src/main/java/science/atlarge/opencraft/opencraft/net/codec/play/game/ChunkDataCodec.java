package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.game.ChunkDataMessage;
import science.atlarge.opencraft.opencraft.util.config.ServerConfig;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;

public final class ChunkDataCodec implements Codec<ChunkDataMessage> {

    @Override
    public ChunkDataMessage decode(ByteBuf buffer) throws IOException {

        int x = buffer.readInt();
        int z = buffer.readInt();
        boolean continuous = buffer.readBoolean();
        int mask = ByteBufUtils.readVarInt(buffer);

        int size = ByteBufUtils.readVarInt(buffer);
        ByteBuf data = buffer.readBytes(size);

        int tagCount = ByteBufUtils.readVarInt(buffer);
        List<CompoundTag> tags = new ArrayList<>();
        for (int tagIndex = 0; tagIndex < tagCount; tagIndex++) {
            CompoundTag tag = GlowBufUtils.readCompound(buffer);
            tags.add(tag);
        }

        return new ChunkDataMessage(x, z, continuous, mask, data, tags);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ChunkDataMessage message) {

        buffer.writeInt(message.getX());
        buffer.writeInt(message.getZ());
        buffer.writeBoolean(message.isContinuous());
        ByteBufUtils.writeVarInt(buffer, message.getPrimaryMask());

        ByteBuf data = message.getData();
        try {
            ByteBufUtils.writeVarInt(buffer, data.writerIndex());
            buffer.writeBytes(data, 0, data.readableBytes());
        } finally {
            if (!ServerConfig.getInstance().getBoolean(ServerConfig.Key.OPENCRAFT_KLUDGE_CHUNKCACHE)) {
                data.release();
            }
        }

        ByteBufUtils.writeVarInt(buffer, message.getBlockEntities().size());
        for (CompoundTag tag : message.getBlockEntities()) {
            GlowBufUtils.writeCompound(buffer, tag);
        }

        return buffer;
    }
}
