package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.IncomingChatMessage;

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
