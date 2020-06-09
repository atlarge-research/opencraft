package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.List;
import net.glowstone.entity.meta.MetadataMap.Entry;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;

public final class EntityMetadataCodec implements Codec<EntityMetadataMessage> {

    @Override
    public EntityMetadataMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        List<Entry> metadata = GlowBufUtils.readMetadata(buffer);
        return new EntityMetadataMessage(id, metadata);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, EntityMetadataMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        GlowBufUtils.writeMetadata(buffer, message.getEntries());
        return buffer;
    }
}
