package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.ChatMessage;

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
