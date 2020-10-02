package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import science.atlarge.opencraft.opencraft.entity.meta.MetadataIndex;
import science.atlarge.opencraft.opencraft.entity.meta.MetadataMap;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnMobMessage;
import org.bukkit.Location;

public class SpawnMobCodecTest extends CodecTest<SpawnMobMessage> {

    @Override
    protected Codec<SpawnMobMessage> createCodec() {
        return new SpawnMobCodec();
    }

    @Override
    protected SpawnMobMessage createMessage() {
        Location location = new Location(null, 5.0, 6.0, 7.0);
        List<MetadataMap.Entry> entries = new ArrayList<>();
        entries.add(new MetadataMap.Entry(MetadataIndex.STATUS, (byte) 1));
        return new SpawnMobMessage(1, UUID.randomUUID(), 3, location, entries);
    }
}
