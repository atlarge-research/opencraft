package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.PingMessage;

public class PingCodecTest extends CodecTest<PingMessage> {

    @Override
    protected Codec<PingMessage> createCodec() {
        return new PingCodec();
    }

    @Override
    protected PingMessage createMessage() {
        return new PingMessage(1);
    }
}
