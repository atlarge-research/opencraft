package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.PlayParticleMessage;

public class PlayParticleCodecTest extends CodecTest<PlayParticleMessage> {

    @Override
    protected Codec<PlayParticleMessage> createCodec() {
        return new PlayParticleCodec();
    }

    @Override
    protected PlayParticleMessage createMessage() {
        return new PlayParticleMessage(1, true, 2.0f, 3.0f, 4.0f, 6.0f, 7.0f, 8.0f, 9.0f, 2, new int[]{ 1, 2 });
    }
}
