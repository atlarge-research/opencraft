package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.PlayerLookMessage;

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
