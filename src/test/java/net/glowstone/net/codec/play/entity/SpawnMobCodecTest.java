package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.SpawnMobMessage;
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
        entries.add(new MetadataMap.Entry(MetadataIndex.TAMEABLEAANIMAL_STATUS, 9));
        return new SpawnMobMessage(1, UUID.randomUUID(), 3, location, entries);
    }
}
