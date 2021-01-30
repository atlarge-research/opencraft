package science.atlarge.opencraft.opencraft.net.codec.login;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.login.EncryptionKeyResponseMessage;

public class EncryptionKeyResponseCodecTest extends CodecTest<EncryptionKeyResponseMessage> {

    @Override
    protected Codec<EncryptionKeyResponseMessage> createCodec() {
        return new EncryptionKeyResponseCodec();
    }

    @Override
    protected EncryptionKeyResponseMessage createMessage() {
        byte[] sharedSecret = { 1, 2 };
        byte[] verifyToken = { 3, 4 };
        return new EncryptionKeyResponseMessage(sharedSecret, verifyToken);
    }
}
