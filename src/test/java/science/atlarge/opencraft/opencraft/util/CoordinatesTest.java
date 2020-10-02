package science.atlarge.opencraft.opencraft.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class CoordinatesTest {

    @Test
    void getX() {
        final double x = 4.0;
        final double z = 5.0;

        Coordinates coordinates = new Coordinates(x, z);

        assertEquals(x, coordinates.getX());
    }

    @Test
    void setX() {
        final double x = 4.0;
        final double z = 5.0;

        Coordinates coordinates = new Coordinates(0.0, z);
        coordinates.setX(x);

        assertEquals(x, coordinates.getX());
    }

    @Test
    void getZ() {
        final double x = 4.0;
        final double z = 5.0;

        Coordinates coordinates = new Coordinates(x, z);

        assertEquals(z, coordinates.getZ());
    }

    @Test
    void setZ() {
        final double x = 4.0;
        final double z = 5.0;

        Coordinates coordinates = new Coordinates(x, 0.0);
        coordinates.setZ(z);

        assertEquals(z, coordinates.getZ());
    }

    @Test
    void getBlock() {
        final double x = 3.0;
        final double z = 7.0;
        final int blockX = 3;
        final int blockZ = 7;

        Coordinates coordinates = new Coordinates(x, z);

        assertEquals(blockX, coordinates.getBlockX());
        assertEquals(blockZ, coordinates.getBlockZ());
    }

    @Test
    void getBlockZ() {
        final double x = 3.0;
        final double z = 7.0;
        final int blockZ = 7;

        Coordinates coordinates = new Coordinates(x, z);

        assertEquals(blockZ, coordinates.getBlockZ());
    }

    @Test
    void getChunk() {
        final double x = 32.0;
        final double z = 31.5;
        final int chunkX = 2;
        final int chunkZ = 1;

        Coordinates coordinates = new Coordinates(x, z);

        assertEquals(chunkX, coordinates.getChunkX());
        assertEquals(chunkZ, coordinates.getChunkZ());
    }

    @Test
    void squaredDistance() {
        final double x = 1.0;
        final double z = 3.0;
        final double xCompare = 4.0;
        final double zCompare = 7.0;
        final double distanceSquared = 25.0;

        Coordinates coords = new Coordinates(x, z);
        Coordinates coordsCompare = new Coordinates(xCompare, zCompare);

        assertEquals(distanceSquared, coords.squaredDistance(coordsCompare));
    }

    @Test
    void distance() {
        final double x = 1.0;
        final double z = 3.0;
        final double xCompare = 4.0;
        final double zCompare = 7.0;
        final double distance = 5.0;

        Coordinates coords = new Coordinates(x, z);
        Coordinates coordsCompare = new Coordinates(xCompare, zCompare);

        assertEquals(distance, coords.distance(coordsCompare));
    }

    @Test
    void testEqualsSame() {
        final double x = 1.0;
        final double z = 3.0;
        final Coordinates coords = new Coordinates(x, z);

        assertEquals(coords, coords);
    }

    @Test
    void testNotEqualsNull() {
        final double x = 1.0;
        final double z = 3.0;
        final Coordinates coords = new Coordinates(x, z);

        assertNotEquals(coords, null);
    }

    @Test
    void testNotEqualsObject() {
        final double x = 1.0;
        final double z = 3.0;
        final Coordinates coords = new Coordinates(x, z);

        assertNotEquals(coords, "this is a string, not a coordinates object");
    }

    @Test
    void testNotEqualsX() {
        final double x = 1.0;
        final double z = 3.0;
        final double modifiedX = 4.0;
        final Coordinates coords = new Coordinates(x, z);
        final Coordinates compare = new Coordinates(modifiedX, z);

        assertNotEquals(coords, compare);
    }

    @Test
    void testNotEqualsZ() {
        final double x = 1.0;
        final double z = 3.0;
        final double modifiedZ = 5.0;
        final Coordinates coords = new Coordinates(x, z);
        final Coordinates compare = new Coordinates(modifiedZ, z);

        assertNotEquals(coords, compare);
    }

    @Test
    void testEquals() {
        final double x = 1.0;
        final double z = 3.0;
        final Coordinates coords = new Coordinates(x, z);
        final Coordinates compare = new Coordinates(x, z);

        assertEquals(coords, compare);
    }

    @Test
    void testToString() {
        final double x = 12.0;
        final double z = 30.0;
        final Coordinates coords = new Coordinates(x, z);

        assertEquals("Coordinates{x=12.0,z=30.0}", coords.toString());
    }
}
