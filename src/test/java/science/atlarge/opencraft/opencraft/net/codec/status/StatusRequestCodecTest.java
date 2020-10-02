package science.atlarge.opencraft.opencraft.net.codec.status;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.status.StatusRequestMessage;

public class StatusRequestCodecTest extends CodecTest<StatusRequestMessage> {

    @Override
    protected Codec<StatusRequestMessage> createCodec() {
        return new StatusRequestCodec();
    }

    @Override
    protected StatusRequestMessage createMessage() {
        return new StatusRequestMessage();
    }
}
