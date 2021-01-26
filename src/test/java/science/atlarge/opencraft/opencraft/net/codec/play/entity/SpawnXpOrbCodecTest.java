package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnXpOrbMessage;

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
