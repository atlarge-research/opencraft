package net.glowstone.net.codec.status;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.status.StatusRequestMessage;

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
