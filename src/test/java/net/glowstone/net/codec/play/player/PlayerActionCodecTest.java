package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.PlayerActionMessage;

public class PlayerActionCodecTest extends CodecTest<PlayerActionMessage> {

    @Override
    protected Codec<PlayerActionMessage> createCodec() {
        return new PlayerActionCodec();
    }

    @Override
    protected PlayerActionMessage createMessage() {
        return new PlayerActionMessage(1, 2, 3);
    }
}
