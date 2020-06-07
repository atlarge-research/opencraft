package net.glowstone.net.codec.status;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.status.StatusPingMessage;

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
