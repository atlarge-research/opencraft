package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.SignEditorMessage;

public class SignEditorCodecTest extends CodecTest<SignEditorMessage> {

    @Override
    protected Codec<SignEditorMessage> createCodec() {
        return new SignEditorCodec();
    }

    @Override
    protected SignEditorMessage createMessage() {
        return new SignEditorMessage(1, 2, 3);
    }
}
