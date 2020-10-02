package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.inv.OpenWindowMessage;

public class OpenWindowCodecTest extends CodecTest<OpenWindowMessage> {

    @Override
    protected Codec<OpenWindowMessage> createCodec() {
        return new OpenWindowCodec();
    }

    @Override
    protected OpenWindowMessage createMessage() {
        return new OpenWindowMessage(1, "two", "three", 4);
    }
}
