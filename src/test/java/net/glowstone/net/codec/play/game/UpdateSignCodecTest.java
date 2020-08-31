package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.UpdateSignMessage;
import net.glowstone.util.TextMessage;

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
