package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.RespawnMessage;

public class RespawnCodecTest extends CodecTest<RespawnMessage> {

    @Override
    protected Codec<RespawnMessage> createCodec() {
        return new RespawnCodec();
    }

    @Override
    protected RespawnMessage createMessage() {
        return new RespawnMessage(1, 2, 3, "four");
    }
}
