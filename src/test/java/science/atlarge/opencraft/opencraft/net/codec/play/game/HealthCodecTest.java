package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.HealthMessage;

public class HealthCodecTest extends CodecTest<HealthMessage> {

    @Override
    protected Codec<HealthMessage> createCodec() {
        return new HealthCodec();
    }

    @Override
    protected HealthMessage createMessage() {
        return new HealthMessage(1.0f, 2, 3.0f);
    }
}
