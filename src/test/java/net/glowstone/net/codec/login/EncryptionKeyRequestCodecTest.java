package net.glowstone.net.codec.login;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.login.EncryptionKeyRequestMessage;

public class EncryptionKeyRequestCodecTest extends CodecTest<EncryptionKeyRequestMessage> {

    @Override
    protected Codec<EncryptionKeyRequestMessage> createCodec() {
        return new EncryptionKeyRequestCodec();
    }

    @Override
    protected EncryptionKeyRequestMessage createMessage() {
        String sessionId = "one";
        byte[] publicKey = { 2, 3 };
        byte[] verifyToken = { 4, 5 };
        return new EncryptionKeyRequestMessage(sessionId, publicKey, verifyToken);
    }
}
