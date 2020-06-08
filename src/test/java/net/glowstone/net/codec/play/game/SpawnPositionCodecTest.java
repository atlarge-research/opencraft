package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.SpawnPositionMessage;

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
