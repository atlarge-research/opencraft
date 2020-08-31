package net.glowstone.net.codec.login;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.login.EncryptionKeyResponseMessage;

public class EncryptionKeyResponseCodecTest extends CodecTest<EncryptionKeyResponseMessage> {

    @Override
    protected Codec<EncryptionKeyResponseMessage> createCodec() {
        return new EncryptionKeyResponseCodec();
    }

    @Override
    protected EncryptionKeyResponseMessage createMessage() {
        byte[] sharedSecret = {1, 2};
        byte[] verifyToken = {3, 4};
        return new EncryptionKeyResponseMessage(sharedSecret, verifyToken);
    }
}
