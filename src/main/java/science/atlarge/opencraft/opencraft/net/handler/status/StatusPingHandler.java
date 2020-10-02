package science.atlarge.opencraft.opencraft.net.handler.status;

import com.flowpowered.network.MessageHandler;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.status.StatusPingMessage;

public final class StatusPingHandler implements MessageHandler<GlowSession, StatusPingMessage> {

    @Override
    public void handle(GlowSession session, StatusPingMessage message) {
        session.send(message);
    }
}
