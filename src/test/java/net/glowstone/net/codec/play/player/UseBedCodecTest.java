package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.UseBedMessage;

public class UseBedCodecTest extends CodecTest<UseBedMessage> {

    @Override
    protected Codec<UseBedMessage> createCodec() {
        return new UseBedCodec();
    }

    @Override
    protected UseBedMessage createMessage() {
        return new UseBedMessage(1, 2, 3, 4);
    }
}
