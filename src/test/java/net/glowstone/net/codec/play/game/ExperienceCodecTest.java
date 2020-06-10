package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.ExperienceMessage;

public class ExperienceCodecTest extends CodecTest<ExperienceMessage> {

    @Override
    protected Codec<ExperienceMessage> createCodec() {
        return new ExperienceCodec();
    }

    @Override
    protected ExperienceMessage createMessage() {
        return new ExperienceMessage(1.0f, 2, 3);
    }
}
