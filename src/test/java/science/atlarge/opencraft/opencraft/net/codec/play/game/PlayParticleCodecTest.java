package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.PlayParticleMessage;

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
