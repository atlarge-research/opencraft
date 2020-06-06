package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.glowstone.net.message.play.game.BlockChangeMessage;
import net.glowstone.net.message.play.game.MultiBlockChangeMessage;

public final class MultiBlockChangeCodec implements Codec<MultiBlockChangeMessage> {

    @Override
    public MultiBlockChangeMessage decode(ByteBuf buffer) throws IOException {

        int chunkX = buffer.readInt();
        int chunkZ = buffer.readInt();

        int size = ByteBufUtils.readVarInt(buffer);
        List<BlockChangeMessage> records = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {

            int position = buffer.readShort();
            int blockX = position >> 12;
            int blockZ = position >> 8;
            int blockY = position;

            int type = ByteBufUtils.readVarInt(buffer);

            BlockChangeMessage record = new BlockChangeMessage(blockX, blockY, blockZ, type);
            records.add(record);
        }

        return new MultiBlockChangeMessage(chunkX, chunkZ, records);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, MultiBlockChangeMessage message) {

        List<BlockChangeMessage> records = message.getRecords();

        buffer.writeInt(message.getChunkX());
        buffer.writeInt(message.getChunkZ());
        ByteBufUtils.writeVarInt(buffer, records.size());

        for (BlockChangeMessage record : records) {
            int position = (record.getX() & 0xF) << 12 | (record.getZ() & 0xF) << 8 | record.getY() & 0xFF;
            buffer.writeShort(position);
            ByteBufUtils.writeVarInt(buffer, record.getType());
        }

        return buffer;
    }
}
