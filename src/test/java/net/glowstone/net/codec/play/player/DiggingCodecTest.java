package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.DiggingMessage;

public class DiggingCodecTest extends CodecTest<DiggingMessage> {

    @Override
    protected Codec<DiggingMessage> createCodec() {
        return new DiggingCodec();
    }

    @Override
    protected DiggingMessage createMessage() {
        return new DiggingMessage(1, 2, 3, 4, 5);
    }
}
