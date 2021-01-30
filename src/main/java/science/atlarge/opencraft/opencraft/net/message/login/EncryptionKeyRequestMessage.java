package science.atlarge.opencraft.opencraft.net.message.login;

import com.flowpowered.network.Message;
import lombok.Data;

@Data
public final class EncryptionKeyRequestMessage implements Message {

    private final String sessionId;
    private final byte[] publicKey;
    private final byte[] verifyToken;

}
