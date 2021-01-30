package science.atlarge.opencraft.opencraft.net.handler.play.player;

import com.flowpowered.network.MessageHandler;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.play.player.SteerVehicleMessage;

public final class SteerVehicleHandler implements MessageHandler<GlowSession, SteerVehicleMessage> {

    @Override
    public void handle(GlowSession session, SteerVehicleMessage message) {
        GlowPlayer player = session.getPlayer();

        if (message.isUnmount() && player.isInsideVehicle()) {
            player.leaveVehicle();
        }
        // todo
    }
}
