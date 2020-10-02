package science.atlarge.opencraft.opencraft.net.handler.play.inv;

import com.flowpowered.network.MessageHandler;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.play.inv.CloseWindowMessage;

public final class CloseWindowHandler implements MessageHandler<GlowSession, CloseWindowMessage> {

    @Override
    public void handle(GlowSession session, CloseWindowMessage message) {
        // closing the inventory will drop any items as needed
        session.getPlayer().closeInventory();
    }
}
