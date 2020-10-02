package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.AttachEntityMessage;

public class AttachEntityCodecTest extends CodecTest<AttachEntityMessage> {

    @Override
    protected Codec<AttachEntityMessage> createCodec() {
        return new AttachEntityCodec();
    }

    @Override
    protected AttachEntityMessage createMessage() {
        return new AttachEntityMessage(1, 2);
    }
}
