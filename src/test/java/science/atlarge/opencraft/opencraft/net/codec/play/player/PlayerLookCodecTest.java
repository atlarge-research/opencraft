package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerLookMessage;

public class PlayerLookCodecTest extends CodecTest<PlayerLookMessage> {

    @Override
    protected Codec<PlayerLookMessage> createCodec() {
        return new PlayerLookCodec();
    }

    @Override
    protected PlayerLookMessage createMessage() {
        return new PlayerLookMessage(1.0f, 2.0f, false);
    }
}
