package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.TimeMessage;

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
