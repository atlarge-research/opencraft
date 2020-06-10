package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.IncomingChatMessage;

public class IncomingChatCodecTest extends CodecTest<IncomingChatMessage> {

    @Override
    protected Codec<IncomingChatMessage> createCodec() {
        return new IncomingChatCodec();
    }

    @Override
    protected IncomingChatMessage createMessage() {
        return new IncomingChatMessage("one");
    }
}
