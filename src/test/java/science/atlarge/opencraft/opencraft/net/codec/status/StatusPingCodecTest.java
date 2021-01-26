package science.atlarge.opencraft.opencraft.net.codec.status;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.status.StatusPingMessage;

public class StatusPingCodecTest extends CodecTest<StatusPingMessage> {

    @Override
    protected Codec<StatusPingMessage> createCodec() {
        return new StatusPingCodec();
    }

    @Override
    protected StatusPingMessage createMessage() {
        return new StatusPingMessage(1);
    }
}
