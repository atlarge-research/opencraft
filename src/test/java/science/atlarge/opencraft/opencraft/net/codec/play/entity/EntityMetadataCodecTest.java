package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.List;
import science.atlarge.opencraft.opencraft.entity.meta.MetadataMap;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityMetadataMessage;

public class EntityMetadataCodecTest extends CodecTest<EntityMetadataMessage> {

    @Override
    protected Codec<EntityMetadataMessage> createCodec() {
        return new EntityMetadataCodec();
    }

    @Override
    protected EntityMetadataMessage createMessage() {
        List<MetadataMap.Entry> entries = new ArrayList<>();
        return new EntityMetadataMessage(1, entries);
    }
}
