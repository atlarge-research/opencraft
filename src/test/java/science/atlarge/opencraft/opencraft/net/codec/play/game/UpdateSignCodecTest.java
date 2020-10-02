package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.UpdateSignMessage;
import science.atlarge.opencraft.opencraft.util.TextMessage;

public class UpdateSignCodecTest extends CodecTest<UpdateSignMessage> {

    @Override
    protected Codec<UpdateSignMessage> createCodec() {
        return new UpdateSignCodec();
    }

    @Override
    protected UpdateSignMessage createMessage() {
        TextMessage[] lines = new TextMessage[] {
                new TextMessage("one"),
                new TextMessage("two"),
                new TextMessage("three"),
                new TextMessage("four")
        };
        return new UpdateSignMessage(1, 2, 3, lines);
    }
}
