package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.PlayerUpdateMessage;

public class PlayerUpdateCodecTest extends CodecTest<PlayerUpdateMessage> {

    @Override
    protected Codec<PlayerUpdateMessage> createCodec() {
        return new PlayerUpdateCodec();
    }

    @Override
    protected PlayerUpdateMessage createMessage() {
        return new PlayerUpdateMessage(false);
    }
}
