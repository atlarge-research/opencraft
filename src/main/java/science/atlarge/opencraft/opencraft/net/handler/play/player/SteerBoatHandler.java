package science.atlarge.opencraft.opencraft.net.handler.play.player;

import com.flowpowered.network.MessageHandler;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.entity.objects.GlowBoat;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.play.player.SteerBoatMessage;

public class SteerBoatHandler implements MessageHandler<GlowSession, SteerBoatMessage> {

    @Override
    public void handle(GlowSession session, SteerBoatMessage message) {
        GlowPlayer player = session.getPlayer();

        if (!player.isInsideVehicle()) {
            return;
        }

        if (!(player.getVehicle() instanceof GlowBoat)) {
            return;
        }

        GlowBoat boat = (GlowBoat) player.getVehicle();
        boat.setRightPaddleTurning(message.isRightPaddleTurning());
        boat.setLeftPaddleTurning(message.isLeftPaddleTurning());
    }
}
