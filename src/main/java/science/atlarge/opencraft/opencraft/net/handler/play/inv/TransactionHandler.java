package science.atlarge.opencraft.opencraft.net.handler.play.inv;

import com.flowpowered.network.MessageHandler;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.play.inv.TransactionMessage;

public final class TransactionHandler implements MessageHandler<GlowSession, TransactionMessage> {

    @Override
    public void handle(GlowSession session, TransactionMessage message) {
        //GlowServer.logger.info(session + ": " + message);
    }
}
