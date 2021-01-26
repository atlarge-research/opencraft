package science.atlarge.opencraft.opencraft.net.handler.play.inv;

import com.flowpowered.network.MessageHandler;
import science.atlarge.opencraft.opencraft.EventFactory;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.play.inv.HeldItemMessage;
import org.bukkit.event.player.PlayerItemHeldEvent;

public final class HeldItemHandler implements MessageHandler<GlowSession, HeldItemMessage> {

    @Override
    public void handle(GlowSession session, HeldItemMessage message) {
        int slot = message.getSlot();
        if (slot < 0 || slot > 8) {
            // sanity check
            return;
        }

        GlowPlayer player = session.getPlayer();
        int oldSlot = player.getInventory().getHeldItemSlot();
        if (slot == oldSlot) {
            // ignore
            return;
        }

        PlayerItemHeldEvent event = new PlayerItemHeldEvent(player, oldSlot, slot);
        EventFactory.getInstance().callEvent(event);

        if (!event.isCancelled()) {
            player.getInventory().setRawHeldItemSlot(slot);
        } else {
            // sends a packet to switch back to the previous held slot
            player.getInventory().setHeldItemSlot(oldSlot);
        }
    }
}
