package science.atlarge.opencraft.opencraft.net.protocol;

import science.atlarge.opencraft.opencraft.net.codec.status.StatusPingCodec;
import science.atlarge.opencraft.opencraft.net.codec.status.StatusRequestCodec;
import science.atlarge.opencraft.opencraft.net.codec.status.StatusResponseCodec;
import science.atlarge.opencraft.opencraft.net.handler.status.StatusPingHandler;
import science.atlarge.opencraft.opencraft.net.handler.status.StatusRequestHandler;
import science.atlarge.opencraft.opencraft.net.message.status.StatusPingMessage;
import science.atlarge.opencraft.opencraft.net.message.status.StatusRequestMessage;
import science.atlarge.opencraft.opencraft.net.message.status.StatusResponseMessage;

public class StatusProtocol extends GlowProtocol {

    /**
     * Creates the protocol instance for {@link StatusPingMessage}, {@link StatusRequestMessage} and
     * {@link StatusResponseMessage}.
     */
    public StatusProtocol() {
        super("STATUS", 2);

        inbound(0x00, StatusRequestMessage.class, StatusRequestCodec.class,
            StatusRequestHandler.class);
        inbound(0x01, StatusPingMessage.class, StatusPingCodec.class, StatusPingHandler.class);

        outbound(0x00, StatusResponseMessage.class, StatusResponseCodec.class);
        outbound(0x01, StatusPingMessage.class, StatusPingCodec.class);
    }
}
