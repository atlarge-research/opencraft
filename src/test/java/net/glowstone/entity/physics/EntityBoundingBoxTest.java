package net.glowstone.entity.physics;

import net.glowstone.util.Vectors;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntityBoundingBoxTest {

    static class BoundingBoxProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(BoundingBox.fromCorners(new Vector(0, 0, 0), new Vector(1, 1, 1))),
                    Arguments.of(BoundingBox.fromCorners(new Vector(-1, -1, -1), new Vector(1, 1, 1))),
                    Arguments.of(BoundingBox.fromCorners(new Vector(-0.5, -0.5, -0.5),
                            new Vector(0.5, 0.5, 0.5)))
            );
        }
    }


    private EntityBoundingBox box;
    private EntityBoundingBox box1;
    private EntityBoundingBox box2;
    private EntityBoundingBox box3;

    @Before
    public void setup() {
        box = new EntityBoundingBox(1.0, 1.0);
        box1 = new EntityBoundingBox(2.0, 2.0);
        box2 = new EntityBoundingBox(1.0, 2.0, 1.0);
        box3 = new EntityBoundingBox(2.0, 2.0, 1.0);

        box.setCenter(0, 0, 0);
        box1.setCenter(0, 0, 0);
        box2.setCenter(0, 0, 0);
        box3.setCenter(0, 0, 0);

    }

    @Test
    public void getBroadPhaseNegativeTest() {
        Vector velocity = new Vector(-1.0, -1.0, -1.0);
        BoundingBox broadBox = box.getBroadPhase(velocity);
        Vector expectedMin = new Vector(-1.5, -1, -1.5);
        Vector expectedMax = new Vector(0.5, 1, 0.5);

        assertTrue(Vectors.equals(expectedMin, broadBox.minCorner));
        assertTrue(Vectors.equals(expectedMax, broadBox.maxCorner));
    }

    @Test
    public void getBroadPhasePositiveTest() {
        Vector velocity = new Vector(1.0, 1.0, 1.0);
        BoundingBox broadBox = box.getBroadPhase(velocity);
        Vector expectedMin = new Vector(-0.5, 0, -0.5);
        Vector expectedMax = new Vector(1.5, 2, 1.5);

        assertTrue(Vectors.equals(expectedMin, broadBox.minCorner));
        assertTrue(Vectors.equals(expectedMax, broadBox.maxCorner));
    }

    @ParameterizedTest
    @ArgumentsSource(BoundingBoxProvider.class)
    public void sweptAABBNegativeVelNoHitTest(BoundingBox collisionBox) {
        EntityBoundingBox localBox = new EntityBoundingBox(1.0, 1.0);
        localBox.setCenter(5, 0, 5);
        double distance = localBox.sweptAxisAlignedBoundingBox(new Vector(0.1, -1, 0.1), collisionBox).getLeft();
        assertEquals(distance, 1.0, Double.MIN_VALUE);
    }

    @ParameterizedTest
    @ArgumentsSource(BoundingBoxProvider.class)
    public void sweptAABBNegativeVelHitVectorTest(BoundingBox collisionBox) {
        EntityBoundingBox localBox = new EntityBoundingBox(1.0, 1.0);
        localBox.setCenter(0, 2, 0);
        double distance = localBox.sweptAxisAlignedBoundingBox(new Vector(0, -2, 0), collisionBox).getLeft();
        assertTrue(distance < 1.0);
    }

    @ParameterizedTest
    @ArgumentsSource(BoundingBoxProvider.class)
    public void sweptAABBPositiveVectorTest(BoundingBox collisionBox) {
        EntityBoundingBox localBox = new EntityBoundingBox(1.0, 1.0);
        localBox.setCenter(0, -2, 0);
        double distance = localBox.sweptAxisAlignedBoundingBox(new Vector(0, 2, 0), collisionBox).getLeft();
        assertTrue(distance < 1.0);
    }

    @ParameterizedTest
    @ArgumentsSource(BoundingBoxProvider.class)
    public void sweptAABBNoVelocityVectorTest(BoundingBox collisionBox) {
        EntityBoundingBox localBox = new EntityBoundingBox(1.0, 1.0);
        localBox.setCenter(0, -2, 0);
        double distance = localBox.sweptAxisAlignedBoundingBox(new Vector(0, 0, 0), collisionBox).getLeft();
        assertEquals(1.0, distance, Double.MIN_VALUE);
    }

    @ParameterizedTest
    @ArgumentsSource(BoundingBoxProvider.class)
    public void sweptAABBXVelTest(BoundingBox collisionBox) {
        EntityBoundingBox localBox = new EntityBoundingBox(1.0, 1.0);
        localBox.setCenter(-2, 0, 0);
        double distance = localBox.sweptAxisAlignedBoundingBox(new Vector(2, 0, 0), collisionBox).getLeft();
        assertTrue(distance < 1.0);
    }

    @ParameterizedTest
    @ArgumentsSource(BoundingBoxProvider.class)
    public void sweptAABBYVelTest(BoundingBox collisionBox) {
        EntityBoundingBox localBox = new EntityBoundingBox(1.0, 1.0);
        localBox.setCenter(0, 0, -2);
        double distance = localBox.sweptAxisAlignedBoundingBox(new Vector(0, 0, 2), collisionBox).getLeft();
        assertTrue(distance < 1.0);
    }

    @Test
    public void createCopyAtTest(){
        Location loc = mock(Location.class);
        when(loc.getX()).thenReturn(0.0);
        when(loc.getY()).thenReturn(0.0);
        when(loc.getZ()).thenReturn(0.0);
        assertNotEquals(box.createCopyAt(loc), box);
    }

    @Test
    public void getSizeTest(){
        assertTrue(Vectors.equals(box.getSize(), new Vector(1.0, 1.0, 1.0)));
    }

}
