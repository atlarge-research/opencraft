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

class ChunkRunnableTest {

    private ChunkRunnable runnable;
    private boolean executed;

    @BeforeEach
    void setUp() {

        executed = false;
        GlowChunk chunk = mock(GlowChunk.class);
        when(chunk.getX()).thenReturn(5);
        when(chunk.getZ()).thenReturn(10);
        when(chunk.getCenterCoordinates()).thenReturn(new Coordinates(5, 10));

        GlowPlayer player = mock(GlowPlayer.class);
        when(player.getCoordinates()).thenReturn(new Coordinates(10, 20));

        runnable = new ChunkRunnable(player, chunk, () -> executed = true);
    }

    /**
     * Verify that the order changes only if the player gets closer to the other chunk.
     */
    @Test
    void updatePriorityCompare() {

        GlowChunk origin = mock(GlowChunk.class);
        when(origin.getX()).thenReturn(0);
        when(origin.getZ()).thenReturn(0);
        when(origin.getCenterCoordinates()).thenReturn(new Coordinates(0, 0));

        GlowPlayer player = runnable.getPlayer();
        ChunkRunnable comparable = new ChunkRunnable(player, origin, () -> executed = true);

        assertTrue(runnable.compareTo(comparable) < 0);

        runnable.updatePriority();
        comparable.updatePriority();

        assertTrue(runnable.compareTo(comparable) < 0);

        when(player.getCoordinates()).thenReturn(new Coordinates(-10, -20));

        runnable.updatePriority();
        comparable.updatePriority();

        assertTrue(runnable.compareTo(comparable) > 0);
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
