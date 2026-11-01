package science.atlarge.opencraft.opencraft.entity.ai;

import science.atlarge.opencraft.opencraft.entity.GlowLivingEntity;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class TransportHelper {

    public static void moveTowards(GlowLivingEntity entity, Location direction) {
        moveTowards(entity, direction, 0.3);
    }

    /**
     * Starts an entity moving horizontally toward a location (may overshoot).
     *
     * @param entity the entity to move
     * @param direction the destination to move toward
     * @param speed the speed to move
     */
    public static void moveTowards(GlowLivingEntity entity, Location direction, double speed) {
        Location location = entity.getLocation();
        double deltaX = (direction.getX() - location.getX());
        double deltaZ = (direction.getZ() - location.getZ());
        entity.setSpeed(speed);
        entity.setMovement(toRelativeMovement(deltaX, deltaZ, location.getYaw()));
    }

    /**
     * Converts a horizontal offset in the world to a unit movement vector relative to a yaw, as
     * used by {@link GlowLivingEntity#setMovement}: x is forward and z is sideways.
     *
     * @param deltaX the offset on the x-axis
     * @param deltaZ the offset on the z-axis
     * @param yaw the yaw of the entity in degrees
     * @return the unit vector, or the zero vector if there is no offset
     */
    static Vector toRelativeMovement(double deltaX, double deltaZ, float yaw) {
        double length = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        if (length < 1e-6) {
            return new Vector();
        }
        double worldX = deltaX / length;
        double worldZ = deltaZ / length;
        double sin = Math.sin(Math.toRadians(yaw));
        double cos = Math.cos(Math.toRadians(yaw));
        return new Vector(-worldX * sin + worldZ * cos, 0, worldX * cos + worldZ * sin);
    }
}
