package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.SpawnPositionMessage;

public class SpawnPositionCodecTest extends CodecTest<SpawnPositionMessage> {

    @Override
    protected Codec<SpawnPositionMessage> createCodec() {
        return new SpawnPositionCodec();
    }

    @Override
    protected SpawnPositionMessage createMessage() {
        return new SpawnPositionMessage(1, 2, 3);
    }
}
