package science.atlarge.opencraft.opencraft.net.handler.play.inv;

import com.flowpowered.network.MessageHandler;
import science.atlarge.opencraft.opencraft.i18n.ConsoleMessages;
import science.atlarge.opencraft.opencraft.inventory.GlowEnchantingInventory;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.play.inv.EnchantItemMessage;
import org.bukkit.inventory.Inventory;

public final class EnchantItemHandler implements MessageHandler<GlowSession, EnchantItemMessage> {

    @Override
    public void handle(GlowSession session, EnchantItemMessage message) {
        Inventory view = session.getPlayer().getOpenInventory().getTopInventory();
        if (view instanceof GlowEnchantingInventory) {
            ((GlowEnchantingInventory) view).onPlayerEnchant(message.getEnchantment());
        } else {
            ConsoleMessages.Info.Enchant.NOT_OPEN.log(session.getPlayer().getName());
        }
    }
}
