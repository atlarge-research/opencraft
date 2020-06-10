package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.HealthMessage;

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
