package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.PlayEffectMessage;

public class PlayEffectCodecTest extends CodecTest<PlayEffectMessage> {

    @Override
    protected Codec<PlayEffectMessage> createCodec() {
        return new PlayEffectCodec();
    }

    @Override
    protected PlayEffectMessage createMessage() {
        return new PlayEffectMessage(1, 2, 3, 4, 5, true);
    }
}
