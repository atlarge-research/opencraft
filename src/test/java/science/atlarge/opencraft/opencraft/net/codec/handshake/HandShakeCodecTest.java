package science.atlarge.opencraft.opencraft.net.codec.handshake;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.handshake.HandshakeMessage;

class HandShakeCodecTest extends CodecTest<HandshakeMessage> {

    @Override
    protected Codec<HandshakeMessage> createCodec() {
        return new HandshakeCodec();
    }

    @Override
    protected HandshakeMessage createMessage() {
        return new HandshakeMessage(1, "two", 3, 4);
    }
}
