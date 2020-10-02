package science.atlarge.opencraft.opencraft.net.handler.play.game;

import com.flowpowered.network.MessageHandler;
import science.atlarge.opencraft.opencraft.i18n.ConsoleMessages;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.play.game.CraftRecipeRequestMessage;

public final class CraftRecipeRequestHandler implements
    MessageHandler<GlowSession, CraftRecipeRequestMessage> {

    @Override
    public void handle(GlowSession session, CraftRecipeRequestMessage message) {
        ConsoleMessages.Warn.Net.CRAFTING_RECIPE_UNSUPPORTED.log(session.getPlayer().getName());
    }
}
