package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.SpawnPlayerMessage;

public class SpawnPlayerCodecTest extends CodecTest<SpawnPlayerMessage> {

    @Override
    protected Codec<SpawnPlayerMessage> createCodec() {
        return new SpawnPlayerCodec();
    }

    @Override
    protected SpawnPlayerMessage createMessage() {
        List<MetadataMap.Entry> entries = new ArrayList<>();
        entries.add(new MetadataMap.Entry(MetadataIndex.STATUS, 2));
        return new SpawnPlayerMessage(1, UUID.randomUUID(), 3.0, 4.0, 5.0, 6, 7, entries);
    }
}
