package science.atlarge.opencraft.opencraft.net.handler.play.player;

import com.flowpowered.network.MessageHandler;
import science.atlarge.opencraft.opencraft.EventFactory;
import science.atlarge.opencraft.opencraft.constants.ResourcePackStatus;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.play.player.ResourcePackStatusMessage;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public final class ResourcePackStatusHandler implements
    MessageHandler<GlowSession, ResourcePackStatusMessage> {

    @Override
    public void handle(GlowSession session, ResourcePackStatusMessage message) {
        GlowPlayer player = session.getPlayer();
        PlayerResourcePackStatusEvent.Status status = ResourcePackStatus
            .getStatus(message.getResult());
        player.setResourcePackStatus(status);
        EventFactory.getInstance().callEvent(
            new PlayerResourcePackStatusEvent(player, status, player.getResourcePackHash()));
    }
}
