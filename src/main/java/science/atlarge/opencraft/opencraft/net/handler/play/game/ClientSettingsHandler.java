package science.atlarge.opencraft.opencraft.net.handler.play.game;

import com.flowpowered.network.MessageHandler;
import science.atlarge.opencraft.opencraft.entity.meta.ClientSettings;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.play.game.ClientSettingsMessage;

public final class ClientSettingsHandler implements
    MessageHandler<GlowSession, ClientSettingsMessage> {

    @Override
    public void handle(GlowSession session, ClientSettingsMessage message) {
        session.getPlayer().setSettings(new ClientSettings(message));
    }
}
