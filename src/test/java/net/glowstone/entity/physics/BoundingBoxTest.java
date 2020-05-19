package net.glowstone.entity.physics;

import net.glowstone.util.Vectors;
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;

import static net.glowstone.entity.physics.BoundingBox.intersects;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoundingBoxTest {

    private BoundingBox box;
    private BoundingBox box1;
    private BoundingBox box2;
    private BoundingBox box3;

    @Before
    public void setup() {
        box = BoundingBox.fromCorners(new Vector(0, 0, 0), new Vector(1, 1, 1));
        box1 = BoundingBox.fromCorners(new Vector(-1, -1, -1), new Vector(0.1, 0.1, 0.1));
        box2 = BoundingBox.fromCorners(new Vector(-2, -2, -2), new Vector(-1, -1.5, -2));
        box3 = BoundingBox.fromCorners(new Vector(-1.5, -1.9, -2.5), new Vector(-1, -1.75, -4));
    }

    @Test
    public void intersectTest() {
        assertTrue(box.intersects(box1));
        assertTrue(intersects(box1, box, Double.MIN_VALUE));
    }

    @Test
    public void intersectTestWithoutIntersect() {
        assertFalse(box2.intersects(box));
        assertFalse(intersects(box, box2, Double.MIN_VALUE));
        assertFalse(box2.intersects(box1));
        assertFalse(intersects(box1, box2, Double.MIN_VALUE));
        assertFalse(box2.intersects(box3));
        assertFalse(intersects(box3, box2, Double.MIN_VALUE));
    }

    @Test
    public void positionAndSizeTest() {
        Vector origin = new Vector(0, 0, 0);
        Vector size = new Vector(1, 1, 1);

        BoundingBox otherBox = BoundingBox.fromPositionAndSize(origin, size);
        assertTrue(Vectors.equals(otherBox.minCorner, origin));
        assertTrue(Vectors.equals(otherBox.maxCorner, size));
    }

    @Test
    public void copyOfTest() {
        assertNotEquals(box, BoundingBox.copyOf(box));
    }

    @Test
    public void getSizeTest() {
        assertTrue(Vectors.equals(box.getSize(), new Vector(1, 1, 1)));
    }
}
