package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.PlayerPositionLookMessage;

public class PlayerPositionLookCodecTest extends CodecTest<PlayerPositionLookMessage> {

    @Override
    protected Codec<PlayerPositionLookMessage> createCodec() {
        return new PlayerPositionLookCodec();
    }

    @Override
    protected PlayerPositionLookMessage createMessage() {
        return new PlayerPositionLookMessage(false, 2, 3, 4, 5.0f, 6.0f);
    }
}
