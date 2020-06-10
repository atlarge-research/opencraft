package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import java.util.UUID;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.SpawnObjectMessage;
import org.bukkit.Location;

public class SpawnObjectCodecTest extends CodecTest<SpawnObjectMessage> {

    @Override
    protected Codec<SpawnObjectMessage> createCodec() {
        return new SpawnObjectCodec();
    }

    @Override
    protected SpawnObjectMessage createMessage() {
        Location location = new Location(null, 5.0, 6.0, 7.0);
        return new SpawnObjectMessage(1, UUID.randomUUID(), 3, location, 8);
    }
}
