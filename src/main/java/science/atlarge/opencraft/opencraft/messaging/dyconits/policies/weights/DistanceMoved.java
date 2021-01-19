package science.atlarge.opencraft.opencraft.messaging.dyconits.policies.weights;

import com.flowpowered.network.Message;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import science.atlarge.opencraft.dyconits.Bounds;
import science.atlarge.opencraft.opencraft.GlowServer;
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

    private final GlowServer server;
    private final Bounds upperNumericalBound;

    public DistanceMoved(GlowServer server, Bounds upperBound) {
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
        } else if (message instanceof EntityRotationMessage) {
            return 0;
        } else if (message instanceof EntityTeleportMessage) {
            EntityTeleportMessage msg = (EntityTeleportMessage) message;
            Entity entity = server.getEntity(msg.getId());
            if (entity instanceof GlowEntity) {
                GlowEntity gEntity = (GlowEntity) entity;
                return (int) (gEntity.getPreviousLocation().distance(new Location(entity.getWorld(), msg.getX(), msg.getY(), msg.getZ())) * 32);
            }
            return upperNumericalBound.getNumerical();
        } else {
            return 0;
        }
    }
}
