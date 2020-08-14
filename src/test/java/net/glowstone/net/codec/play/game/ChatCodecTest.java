package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.ChatMessage;

public class ChatCodecTest extends CodecTest<ChatMessage> {

    @Override
    protected Codec<ChatMessage> createCodec() {
        return new ChatCodec();
    }

    @Override
    protected ChatMessage createMessage() {
        return new ChatMessage("one");
    }
}
