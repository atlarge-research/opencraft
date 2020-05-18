package net.glowstone.executor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import net.glowstone.chunk.GlowChunk;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.util.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for verifying that the ChunkRunnable functionality works properly.
 */
class ChunkRunnableTest {

    private GlowPlayer player;
    private final int chunkX = -1;
    private final int chunkZ = -5;
    private ChunkRunnable runnable;
    private boolean executed;

    /**
     * Initialize the required objects and values for each test.
     */
    @BeforeEach
    void setUp() {
        executed = false;
        GlowChunk chunk = mock(GlowChunk.class);
        when(chunk.getCenterCoordinates()).thenReturn(new Coordinates(chunkX, chunkZ));
        when(chunk.getX()).thenReturn(chunkX);
        when(chunk.getZ()).thenReturn(chunkZ);

        player = mock(GlowPlayer.class);
        Coordinates playerCoords = new Coordinates(10, 20);
        when(player.getCoordinates()).thenReturn(playerCoords);

        runnable = new ChunkRunnable(player, chunk, () -> executed = true);
    }

    /**
     * Verify that the hasKey function correctly checks if the key is the same.
     */
    @Test
    void hasKey() {
        final GlowChunk.Key originKey = GlowChunk.Key.of(0, 0);
        final GlowChunk.Key farKey = GlowChunk.Key.of(1000, 50);
        final GlowChunk.Key sameKey = GlowChunk.Key.of(chunkX, chunkZ);

        assertFalse(runnable.hasKey(originKey));
        assertFalse(runnable.hasKey(farKey));
        assertTrue(runnable.hasKey(sameKey));
    }

    /**
     * Verify that the hasEntityId function correctly checks if the entity id is the same.
     */
    @Test
    void hasEntityId() {
        final int entityId = 42;
        final int incorrectEntityId = 50;
        when(player.getEntityId()).thenReturn(entityId);

        assertTrue(runnable.hasEntityId(entityId));
        assertFalse(runnable.hasEntityId(incorrectEntityId));
    }

    /**
     * Verify that the order changes only if the player gets closer to the other chunk.
     */
    @Test
    void updatePriorityCompare() {
        GlowChunk otherChunk = mock(GlowChunk.class);
        Coordinates originCoords = new Coordinates(0, 0);
        when(otherChunk.getCenterCoordinates()).thenReturn(originCoords);

        ChunkRunnable comparable = new ChunkRunnable(player, otherChunk, () -> executed = true);

        assertTrue(runnable.compareTo(comparable) > 0);

        runnable.updatePriority();
        comparable.updatePriority();

        assertTrue(runnable.compareTo(comparable) > 0);

        Coordinates negativeCoords = new Coordinates(-100, -10);
        when(player.getCoordinates()).thenReturn(negativeCoords);

        runnable.updatePriority();
        comparable.updatePriority();

        assertFalse(runnable.compareTo(comparable) > 0);
    }

    /**
     * Verify that the runnable is actually executed.
     */
    @Test
    void run() {
        assertFalse(executed);

        runnable.run();

        assertTrue(executed);
    }
}