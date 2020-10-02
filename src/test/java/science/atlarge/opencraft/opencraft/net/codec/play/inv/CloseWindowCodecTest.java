package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.inv.CloseWindowMessage;

public class CloseWindowCodecTest extends CodecTest<CloseWindowMessage> {

    @Override
    protected Codec<CloseWindowMessage> createCodec() {
        return new CloseWindowCodec();
    }

    @Override
    protected CloseWindowMessage createMessage() {
        return new CloseWindowMessage(1);
    }
}
