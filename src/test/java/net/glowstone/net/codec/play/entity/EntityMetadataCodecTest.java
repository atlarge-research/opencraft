package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.List;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;

public class EntityMetadataCodecTest extends CodecTest<EntityMetadataMessage> {

    @Override
    protected Codec<EntityMetadataMessage> createCodec() {
        return new EntityMetadataCodec();
    }

    @Override
    protected EntityMetadataMessage createMessage() {
        List<MetadataMap.Entry> entries = new ArrayList<>();
        entries.add(new MetadataMap.Entry(MetadataIndex.STATUS, 2));
        entries.add(new MetadataMap.Entry(MetadataIndex.ARMORSTAND_FLAGS, 3));
        return new EntityMetadataMessage(1, entries);
    }
}
