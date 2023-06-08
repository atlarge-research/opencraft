package science.atlarge.opencraft.opencraft.messaging.dyconits.policies.weights;

import com.flowpowered.network.Message;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.Server;
import science.atlarge.opencraft.dyconits.Bounds;
import science.atlarge.opencraft.opencraft.entity.GlowEntity;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityRotationMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityTeleportMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.RelativeEntityPositionMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.RelativeEntityPositionRotationMessage;

/**
 * This class weighs messages based on entity displacement.
 * <p>
 * Note that the weights are scaled. Moving one unit in the game receives a message weight of 32.
 * This is useful because packet weights are integers. Using a weight of 1 for a 1 meter avatar displacement effectively
 * prevents using bounds smaller than 1 meter.
 */
public class DistanceMoved implements WeighMessage {

    private final Server server;
    private final Bounds upperNumericalBound;

    public DistanceMoved(Server server, Bounds upperBound) {
        this.server = server;
        this.upperNumericalBound = upperBound;
    }

    @Override
    public int weigh(Message message) {
        if (message instanceof RelativeEntityPositionMessage) {
            RelativeEntityPositionMessage msg = (RelativeEntityPositionMessage) message;
            double x = msg.getDeltaX() / 128.0;
            double y = msg.getDeltaY() / 128.0;
            double z = msg.getDeltaZ() / 128.0;
            return (int) Math.sqrt(x * x + y * y + z * z);
        } else if (message instanceof RelativeEntityPositionRotationMessage) {
            RelativeEntityPositionRotationMessage msg = (RelativeEntityPositionRotationMessage) message;
            double x = msg.getDeltaX() / 128.0;
            double y = msg.getDeltaY() / 128.0;
            double z = msg.getDeltaZ() / 128.0;
            return (int) Math.sqrt(x * x + y * y + z * z);
        } 

        return 0;
    }
}