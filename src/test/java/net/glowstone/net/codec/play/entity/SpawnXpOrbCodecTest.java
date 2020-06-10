package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.SpawnXpOrbMessage;

public class SpawnXpOrbCodecTest extends CodecTest<SpawnXpOrbMessage> {

    @Override
    protected Codec<SpawnXpOrbMessage> createCodec() {
        return new SpawnXpOrbCodec();
    }

    @Override
    protected SpawnXpOrbMessage createMessage() {
        return new SpawnXpOrbMessage(1, 2.0, 3.0, 4.0, (short) 5);
    }
}
