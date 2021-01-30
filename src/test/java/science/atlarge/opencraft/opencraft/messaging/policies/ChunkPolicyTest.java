package science.atlarge.opencraft.opencraft.messaging.policies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test the chunk policy using a mocked world and simplified chunks.
 */
final class ChunkPolicyTest {

    private World world;
    private ChunkPolicy policy;

    /**
     * Setup a mocked world and the chunk policy instance.
     */
    @BeforeEach
    void beforeEach() {

        world = mock(World.class);
        when(world.getChunkAt(any(int.class), any(int.class))).thenAnswer(arguments -> {
            int x = arguments.getArgument(0);
            int z = arguments.getArgument(1);
            return new SimpleChunk(world, x, z);
        });

        policy = new ChunkPolicy(world, 1);
    }

    /**
     * Verify that a player not residing in a world does not have interests in that world.
     */
    @Test
    void noInterestsTest() {

        Player alice = createPlayer(null, 0, 0, 0);
        Set<Chunk> interestSet = policy.computeInterestSet(alice);

        assertTrue(interestSet.isEmpty());
    }

    /**
     * Verify that a player with a view distance of 0 is only interested in a single chunk.
     */
    @Test
    void singleInterestTest() {

        Player alice = createPlayer(world, 0, 0, 0);
        Set<Chunk> interestSet = policy.computeInterestSet(alice);

        Chunk expected = new SimpleChunk(world, 0, 0);
        assertEquals(1, interestSet.size());
        assertTrue(interestSet.contains(expected));
    }

    /**
     * Verify that a player not residing at the origin is interested in the correct chunk.
     */
    @Test
    void singleOffsetInterestTest() {

        Player alice = createPlayer(world, 2, -6, 0);
        Set<Chunk> interestSet = policy.computeInterestSet(alice);

        Chunk expected = new SimpleChunk(world, 2, -6);
        assertEquals(1, interestSet.size());
        assertTrue(interestSet.contains(expected));
    }

    /**
     * Verify that a player with a view distance greater than 0 is interested in multiple chunks.
     */
    @Test
    void multipleInterestsTest() {

        Player alice = createPlayer(world, 0, 0, 1);
        Set<Chunk> interestSet = policy.computeInterestSet(alice);

        Set<Chunk> expected = createChunks(0, 0, alice.getViewDistance());
        assertEquals(expected, interestSet);
    }

    /**
     * Verify that a player not residing at the origin is interested in the correct chunks.
     */
    @Test
    void multipleOffsetInterestsTest() {

        Player alice = createPlayer(world, 2, -6, 1);
        Set<Chunk> interestSet = policy.computeInterestSet(alice);

        Set<Chunk> expected = createChunks(2, -6, alice.getViewDistance());
        assertEquals(expected, interestSet);
    }

    /**
     * Verify that the correct target topic is returned for a chunk type publisher.
     */
    @Test
    void selectChunkTargetTest() {
        Chunk expected = new SimpleChunk(world, 1, 2);
        Iterable<Chunk> targets = policy.selectTargets(expected);
        assertIterableEquals(Collections.singletonList(expected), targets);
    }

    /**
     * Verify that the correct target topic is returned for a block type publisher.
     */
    @Test
    void selectBlockTargetTest() {
        Chunk expected = new SimpleChunk(world, 1, 2);
        Block block = mock(Block.class);
        when(block.getChunk()).thenReturn(expected);
        Iterable<Chunk> targets = policy.selectTargets(block);
        assertIterableEquals(Collections.singletonList(expected), targets);
    }

    /**
     * Verify that the correct target topic is returned for an entity type publisher.
     */
    @Test
    void selectEntityTargetTest() {
        Chunk expected = new SimpleChunk(world, 1, 2);
        Entity entity = mock(Entity.class);
        when(entity.getChunk()).thenReturn(expected);
        Iterable<Chunk> targets = policy.selectTargets(entity);
        assertIterableEquals(Collections.singletonList(expected), targets);
    }

    /**
     * Verify that the correct exception is thrown for an unsupported type publisher.
     */
    @Test
    void selectObjectTargetTest() {
        Object object = new Object();
        assertThrows(UnsupportedOperationException.class, () -> policy.selectTargets(object));
    }

    /**
     * Create a player mock residing at the given world and chunk coordinate.
     *
     * @param world the player's world.
     * @param x the player's chunk's x-coordinate.
     * @param z the player's chunk's z-coordinate.
     * @param viewDistance the player's view distance.
     * @return the player mock.
     */
    private Player createPlayer(World world, int x, int z, int viewDistance) {
        Location location = new Location(world, 16.0 * x, 0.0, 16.0 * z);
        Player player = mock(Player.class);
        when(player.getLocation()).thenReturn(location);
        when(player.getViewDistance()).thenReturn(viewDistance);
        return player;
    }

    /**
     * Generate a set of simple chunks around the given center point.
     *
     * @param centerX the x-coordinate around which to generate chunks.
     * @param centerZ the z-coordinate around which to generate chunks.
     * @param viewDistance the view distance used to determine the radius of generation.
     * @return a set of generated chunks.
     */
    private Set<Chunk> createChunks(int centerX, int centerZ, int viewDistance) {
        Set<Chunk> chunks = new HashSet<>();
        for (int x = centerX - viewDistance; x <= centerX + viewDistance; x++) {
            for (int z = centerZ - viewDistance; z <= centerZ + viewDistance; z++) {
                Chunk chunk = new SimpleChunk(world, x, z);
                chunks.add(chunk);
            }
        }
        return chunks;
    }
}
