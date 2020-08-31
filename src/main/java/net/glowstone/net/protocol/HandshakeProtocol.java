package net.glowstone.net.protocol;

import net.glowstone.net.codec.handshake.HandshakeCodec;
import net.glowstone.net.handler.handshake.HandshakeHandler;
import net.glowstone.net.message.handshake.HandshakeMessage;

public class HandshakeProtocol extends GlowProtocol {

    /**
     * Constructor for the handshake protocol.
     *
     * @param statusProtocol the status protocol.
     * @param loginProtocol  the login protocol.
     */
    public HandshakeProtocol(StatusProtocol statusProtocol, LoginProtocol loginProtocol) {
        super("HANDSHAKE", 0);
        inbound(0x00, HandshakeMessage.class, HandshakeCodec.class,
            new HandshakeHandler(statusProtocol, loginProtocol));
    }
}
