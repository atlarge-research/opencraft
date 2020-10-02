package science.atlarge.opencraft.opencraft.net.protocol;

import science.atlarge.opencraft.opencraft.net.codec.KickCodec;
import science.atlarge.opencraft.opencraft.net.codec.SetCompressionCodec;
import science.atlarge.opencraft.opencraft.net.codec.login.EncryptionKeyRequestCodec;
import science.atlarge.opencraft.opencraft.net.codec.login.EncryptionKeyResponseCodec;
import science.atlarge.opencraft.opencraft.net.codec.login.LoginStartCodec;
import science.atlarge.opencraft.opencraft.net.codec.login.LoginSuccessCodec;
import science.atlarge.opencraft.opencraft.net.handler.login.EncryptionKeyResponseHandler;
import science.atlarge.opencraft.opencraft.net.handler.login.LoginStartHandler;
import science.atlarge.opencraft.opencraft.net.http.HttpClient;
import science.atlarge.opencraft.opencraft.net.message.KickMessage;
import science.atlarge.opencraft.opencraft.net.message.SetCompressionMessage;
import science.atlarge.opencraft.opencraft.net.message.login.EncryptionKeyRequestMessage;
import science.atlarge.opencraft.opencraft.net.message.login.EncryptionKeyResponseMessage;
import science.atlarge.opencraft.opencraft.net.message.login.LoginStartMessage;
import science.atlarge.opencraft.opencraft.net.message.login.LoginSuccessMessage;

public class LoginProtocol extends GlowProtocol {

    /**
     * Creates the instance.
     */
    public LoginProtocol(final HttpClient httpClient) {
        super("LOGIN", 5);

        inbound(0x00, LoginStartMessage.class, LoginStartCodec.class, LoginStartHandler.class);
        inbound(0x01, EncryptionKeyResponseMessage.class, EncryptionKeyResponseCodec.class,
            new EncryptionKeyResponseHandler(httpClient));

        outbound(0x00, KickMessage.class, KickCodec.class);
        outbound(0x01, EncryptionKeyRequestMessage.class, EncryptionKeyRequestCodec.class);
        outbound(0x02, LoginSuccessMessage.class, LoginSuccessCodec.class);
        outbound(0x03, SetCompressionMessage.class, SetCompressionCodec.class);
    }
}
