package net.glowstone.net.codec.handshake;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.handshake.HandshakeMessage;

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
