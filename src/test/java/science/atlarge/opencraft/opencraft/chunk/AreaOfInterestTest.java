package science.atlarge.opencraft.opencraft.chunk;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.GlowWorld;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test class for the AreaOfInterest.
 */
public class AreaOfInterestTest {

    private static final int SERVER_VIEW_DISTANCE = 2;
    private static final int CLIENT_VIEW_DISTANCE = 3;
    private static GlowWorld overworld;
    private static GlowWorld nether;
    private static Location origin;
    private static Location offsetX;
    private static Location offsetZ;

    /**
     * Prepare the required server and world mocks, and create three locations to be used in the getter and comparison
     * tests.
     */
    @BeforeAll
    static void beforeAll() {

        GlowServer server = mock(GlowServer.class);
        when(server.getViewDistance()).thenReturn(SERVER_VIEW_DISTANCE);

        overworld = mock(GlowWorld.class);
        when(overworld.getServer()).thenReturn(server);
        when(overworld.getChunkAt(anyInt(), anyInt())).thenAnswer(invocation -> {
            int x = invocation.getArgument(0);
            int z = invocation.getArgument(1);
            return createChunk(overworld, x, z);
        });

        nether = mock(GlowWorld.class);
        when(nether.getServer()).thenReturn(server);

        origin = new Location(overworld, 0.0, 0.0, 0.0);
        offsetX = new Location(overworld, 16.0, 0.0, 0.0);
        offsetZ = new Location(overworld, 0.0, 0.0, 16.0);
    }

    /**
     * Create a mocked chunk returning the given values from its associated getters.
     *
     * @param world the world the chunk should return.
     * @param x the x-coordinate the chunk should return.
     * @param z the z-coordinate the chunk should return.
     * @return the mocked chunk.
     */
    private static GlowChunk createChunk(GlowWorld world, int x, int z) {
        GlowChunk chunk = mock(GlowChunk.class);
        when(chunk.getWorld()).thenReturn(world);
        when(chunk.getX()).thenReturn(x);
        when(chunk.getZ()).thenReturn(z);
        return chunk;
    }

    /**
     * Verify that an area can be created without throwing an exception.
     */
    @Test
    void construction() {
        assertDoesNotThrow(() -> new AreaOfInterest(origin, CLIENT_VIEW_DISTANCE));
    }

    /**
     * Verify that no area can be created with a null location.
     */
    @Test
    void constructWithNullLocation() {
        assertThrows(IllegalArgumentException.class, () -> new AreaOfInterest(null, 0));
    }

    /**
     * Verify that the world getter returns the correct world.
     */
    @Test
    void getWorld() {
        AreaOfInterest area = new AreaOfInterest(offsetX, CLIENT_VIEW_DISTANCE);
        assertEquals(overworld, area.getWorld());
    }

    /**
     * Verify that the center x getter returns the correctly translated x-coordinate.
     */
    @Test
    void getCenterX() {
        AreaOfInterest area = new AreaOfInterest(offsetX, CLIENT_VIEW_DISTANCE);
        assertEquals(1, area.getCenterX());
    }

    /**
     * Verify that the center z getter return the correctly translated z-coordinate.
     */
    @Test
    void getCenterZ() {
        AreaOfInterest area = new AreaOfInterest(offsetZ, CLIENT_VIEW_DISTANCE);
        assertEquals(1, area.getCenterZ());
    }

    /**
     * Verify that the radius getters returns the correct radius.
     */
    @Test
    void getRadius() {
        AreaOfInterest area = new AreaOfInterest(offsetX, CLIENT_VIEW_DISTANCE);
        assertEquals(2, area.getRadius());
    }

    /**
     * Verify that the radius getters returns the correct radius, even if the client's view distance is smaller than
     * the server's view distance.
     */
    @Test
    void getRadiusMinimalClient() {
        AreaOfInterest area = new AreaOfInterest(offsetX, 0);
        assertEquals(1, area.getRadius());
    }

    /**
     * Verify that a chunk within the area is correctly recognized.
     */
    @Test
    void contains() {
        Chunk chunk = createChunk(overworld, 0, 0);
        AreaOfInterest area = new AreaOfInterest(origin, 0);
        assertTrue(area.contains(chunk));
    }

    /**
     * Verify that a chunk in another dimension is not detected as being in the area.
     */
    @Test
    void notContainsNether() {
        Chunk chunk = createChunk(nether, 0, 0);
        AreaOfInterest area = new AreaOfInterest(origin, 0);
        assertFalse(area.contains(chunk));
    }

    /**
     * Verify that a chunk outside of the area's radius on the x-axis is not detected as being in the area.
     */
    @Test
    void notContainsOffsetX() {
        Chunk chunk = createChunk(overworld, 4, 0);
        AreaOfInterest area = new AreaOfInterest(origin, 0);
        assertFalse(area.contains(chunk));
    }

    /**
     * Verify that a chunk outside of the area's radius on the z-axis is not detected as being in the area.
     */
    @Test
    void notContainsOffsetZ() {
        Chunk chunk = createChunk(overworld, 0, 4);
        AreaOfInterest area = new AreaOfInterest(origin, 0);
        assertFalse(area.contains(chunk));
    }

    /**
     * Verify that the forEach method actually returns chunks.
     */
    @Test
    void forEach() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        area.forEach(Assertions::assertNotNull);
    }

    /**
     * Verify that an area is equal to itself.
     */
    @Test
    void equalsSelf() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        assertEquals(area, area);
    }

    /**
     * Verify that an area is equal to another area with the same parameters.
     */
    @Test
    void equalsOther() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        AreaOfInterest other = new AreaOfInterest(origin, 1);
        assertEquals(area, other);
    }

    /**
     * Verify that an area is not equal to null.
     */
    @Test
    void notEqualsNull() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        assertNotEquals(area, null);
    }

    /**
     * Verify that an area is not equal to an object of another class.
     */
    @Test
    void notEqualsClass() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        assertNotEquals(area, 1);
    }

    /**
     * Verify that an area is not equal to an area with a different x-coordinate as center.
     */
    @Test
    void notEqualsOffsetX() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        AreaOfInterest other = new AreaOfInterest(offsetX, 1);
        assertNotEquals(area, other);
    }

    /**
     * Verify that an area is not equal to an area with a different z-coordinate as center.
     */
    @Test
    void notEqualsOffsetZ() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        AreaOfInterest other = new AreaOfInterest(offsetZ, 1);
        assertNotEquals(area, other);
    }

    /**
     * Verify that an area is not equal to an area with a different radius.
     */
    @Test
    void notEqualsRadius() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        AreaOfInterest other = new AreaOfInterest(origin, 0);
        assertNotEquals(area, other);
    }

    /**
     * Verify that an area is not equal to an area in a different dimension.
     */
    @Test
    void notEqualsDimension() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        Location netherLocation = new Location(nether, 0.0, 0.0, 0.0);
        AreaOfInterest other = new AreaOfInterest(netherLocation, 1);
        assertNotEquals(area, other);
    }

    /**
     * Verify that the area returns a valid iterator.
     */
    @Test
    void getIterator() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        Iterator<GlowChunk> iterator = area.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
    }

    /**
     * Verify that the area returns a valid spliterator.
     */
    @Test
    void getSpliterator() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        Spliterator<GlowChunk> spliterator = area.spliterator();
        assertNotNull(spliterator);
        assertTrue(spliterator.tryAdvance(Assertions::assertNotNull));
    }

    /**
     * Verify that the area returns the correct hashcode.
     */
    @Test
    void getHashCode() {
        AreaOfInterest area = new AreaOfInterest(origin, 1);
        int hashCode = Objects.hash(area.getWorld(), area.getCenterX(), area.getCenterZ(), area.getRadius());
        assertEquals(hashCode, area.hashCode());
    }
}
