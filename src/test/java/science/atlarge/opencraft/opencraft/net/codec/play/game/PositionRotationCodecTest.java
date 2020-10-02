package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.PositionRotationMessage;

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
