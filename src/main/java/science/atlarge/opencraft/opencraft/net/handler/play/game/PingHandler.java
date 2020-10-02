package science.atlarge.opencraft.opencraft.net.handler.play.game;

import com.flowpowered.network.MessageHandler;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.play.game.PingMessage;

public final class PingHandler implements MessageHandler<GlowSession, PingMessage> {

    @Override
    public void handle(GlowSession session, PingMessage message) {
        session.pong(message.getPingId());
    }
}
