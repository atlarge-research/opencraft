package science.atlarge.opencraft.opencraft.net.protocol;

import science.atlarge.opencraft.opencraft.net.codec.handshake.HandshakeCodec;
import science.atlarge.opencraft.opencraft.net.handler.handshake.HandshakeHandler;
import science.atlarge.opencraft.opencraft.net.message.handshake.HandshakeMessage;

public class HandshakeProtocol extends GlowProtocol {

    /**
     * Constructor for the handshake protocol.
     * @param statusProtocol the status protocol.
     * @param loginProtocol the login protocol.
     */
    public HandshakeProtocol(StatusProtocol statusProtocol, LoginProtocol loginProtocol) {
        super("HANDSHAKE", 0);
        inbound(0x00, HandshakeMessage.class, HandshakeCodec.class,
            new HandshakeHandler(statusProtocol, loginProtocol));
    }
}
