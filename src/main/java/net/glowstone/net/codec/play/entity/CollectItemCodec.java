package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.entity.CollectItemMessage;

public final class CollectItemCodec implements Codec<CollectItemMessage> {

    @Override
    public CollectItemMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        int collector = ByteBufUtils.readVarInt(buffer);
        int count = ByteBufUtils.readVarInt(buffer);
        return new CollectItemMessage(id, collector, count);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, CollectItemMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        ByteBufUtils.writeVarInt(buffer, message.getCollector());
        ByteBufUtils.writeVarInt(buffer, message.getCount());
        return buffer;
    }
}
