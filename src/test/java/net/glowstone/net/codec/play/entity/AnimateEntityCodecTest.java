package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.AnimateEntityMessage;

public class AnimateEntityCodecTest extends CodecTest<AnimateEntityMessage> {

    @Override
    protected Codec<AnimateEntityMessage> createCodec() {
        return new AnimateEntityCodec();
    }

    @Override
    protected AnimateEntityMessage createMessage() {
        return new AnimateEntityMessage(1, 2);
    }
}
