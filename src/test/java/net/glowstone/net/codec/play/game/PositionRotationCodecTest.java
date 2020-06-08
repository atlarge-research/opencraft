package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.PositionRotationMessage;

public class PositionRotationCodecTest extends CodecTest<PositionRotationMessage> {

    @Override
    protected Codec<PositionRotationMessage> createCodec() {
        return new PositionRotationCodec();
    }

    @Override
    protected PositionRotationMessage createMessage() {
        return new PositionRotationMessage(1.0, 2.0, 3.0, 4.0f, 5.0f);
    }
}
