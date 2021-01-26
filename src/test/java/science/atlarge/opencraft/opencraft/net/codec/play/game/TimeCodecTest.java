package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.TimeMessage;

public class TimeCodecTest extends CodecTest<TimeMessage> {

    @Override
    protected Codec<TimeMessage> createCodec() {
        return new TimeCodec();
    }

    @Override
    protected TimeMessage createMessage() {
        return new TimeMessage(1, 2);
    }
}
