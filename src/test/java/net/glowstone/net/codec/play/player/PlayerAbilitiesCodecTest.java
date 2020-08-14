package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.PlayerAbilitiesMessage;

public class PlayerAbilitiesCodecTest extends CodecTest<PlayerAbilitiesMessage> {

    @Override
    protected Codec<PlayerAbilitiesMessage> createCodec() {
        return new PlayerAbilitiesCodec();
    }

    @Override
    protected PlayerAbilitiesMessage createMessage() {
        return new PlayerAbilitiesMessage(1, 2.0f, 3.0f);
    }
}
