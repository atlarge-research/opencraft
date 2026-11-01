package science.atlarge.opencraft.opencraft.entity.ai;

import static org.junit.Assert.assertEquals;

import org.bukkit.util.Vector;
import org.junit.Test;

public class TransportHelperTest {

    /**
     * Applies the rotation that {@code GlowLivingEntity} uses to turn a movement relative to the
     * entity into a world velocity.
     */
    private static Vector toWorld(Vector relative, float yaw) {
        double sin = Math.sin(Math.toRadians(yaw));
        double cos = Math.cos(Math.toRadians(yaw));
        return new Vector(relative.getZ() * cos - relative.getX() * sin, 0,
                relative.getX() * cos + relative.getZ() * sin);
    }

    @Test
    public void testMovementPointsTowardsTargetForAnyYaw() {
        for (float yaw = -360; yaw <= 360; yaw += 45) {
            Vector world = toWorld(TransportHelper.toRelativeMovement(3, -4, yaw), yaw);
            assertEquals("x for yaw " + yaw, 0.6, world.getX(), 1e-9);
            assertEquals("z for yaw " + yaw, -0.8, world.getZ(), 1e-9);
        }
    }

    @Test
    public void testMovementIsAUnitVector() {
        Vector relative = TransportHelper.toRelativeMovement(30, 40, 17);
        assertEquals(1.0, relative.length(), 1e-9);
    }

    @Test
    public void testNoOffsetMeansNoMovement() {
        assertEquals(0.0, TransportHelper.toRelativeMovement(0, 0, 90).length(), 0);
    }
}
