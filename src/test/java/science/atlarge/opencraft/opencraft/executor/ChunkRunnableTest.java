package science.atlarge.opencraft.opencraft.executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.stream.Stream;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.block.entity.BlockEntity;
import science.atlarge.opencraft.opencraft.chunk.ChunkManager;
import science.atlarge.opencraft.opencraft.chunk.GlowChunk;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.net.GlowSession;
import science.atlarge.opencraft.opencraft.net.message.play.game.ChunkDataMessage;
import science.atlarge.opencraft.opencraft.util.Coordinates;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ChunkRunnableTest {

    private final int chunkX = 5;
    private final int chunkZ = 10;
    private BlockEntity blockEntityOne;
    private BlockEntity blockEntityTwo;
    private ChunkDataMessage chunkDataMessage;
    private ChunkManager.ChunkLock chunkLock;
    private ChunkManager chunkManager;
    private GlowPlayer player;
    private ChunkRunnable runnable;
    private GlowSession session;
    private GlowWorld world;

    @BeforeEach
    void setUp() {

        blockEntityOne = mock(BlockEntity.class);
        blockEntityTwo = mock(BlockEntity.class);
        chunkDataMessage = new ChunkDataMessage(chunkX, chunkZ, false, 0, null, null);
        chunkManager = mock(ChunkManager.class);
        chunkLock = mock(ChunkManager.ChunkLock.class);
        session = mock(GlowSession.class);
        world = mock(GlowWorld.class);

        when(world.getChunkManager()).thenReturn(chunkManager);

        GlowChunk chunk = mock(GlowChunk.class);
        when(chunk.getX()).thenReturn(chunkX);
        when(chunk.getZ()).thenReturn(chunkZ);
        when(chunk.getCenterCoordinates()).thenReturn(new Coordinates(chunkX, chunkZ));
        when(chunk.getWorld()).thenReturn(world);
        when(chunk.getRawBlockEntities()).thenReturn(Arrays.asList(blockEntityOne, blockEntityTwo));
        when(chunk.toMessage(anyBoolean())).thenReturn(chunkDataMessage);

        player = mock(GlowPlayer.class);
        when(player.getCoordinates()).thenReturn(new Coordinates(8, 14));
        when(player.getChunkLock()).thenReturn(chunkLock);
        when(player.getSession()).thenReturn(session);

        runnable = new ChunkRunnable(player, chunk);
    }

    /**
     * Verify that the runnable is actually executed.
     */
    @ParameterizedTest
    @MethodSource
    void run(Pair<World.Environment, Boolean> parameters) {

        when(world.getEnvironment()).thenReturn(parameters.getLeft());

        runnable.run();

        verify(chunkManager).forcePopulation(chunkX, chunkZ);
        verify(chunkLock).acquire(GlowChunk.Key.of(chunkX, chunkZ));
        verify(runnable.getChunk()).toMessage(parameters.getRight());
        verify(session).send(chunkDataMessage);

        verify(blockEntityOne).update(runnable.getPlayer());
        verify(blockEntityTwo).update(runnable.getPlayer());
    }

    /**
     * Parameters that are used in the run parameterized test.
     *
     * @return The pairs of environment and booleans for the skylight.
     */
    private static Stream<Pair<World.Environment, Boolean>> run() {
        return Stream.of(
                Pair.of(World.Environment.NORMAL, true),
                Pair.of(World.Environment.NETHER, false),
                Pair.of(World.Environment.THE_END, false)
        );
    }

    /**
     * Assert that the priority is always equal to the squared distance after the priority is updated.
     */
    @Test
    void updatePriority() {
        assertEquals(25.0, runnable.getPriority());

        runnable.updatePriority();
        when(player.getCoordinates()).thenReturn(new Coordinates(chunkX, chunkZ));

        assertEquals(25.0, runnable.getPriority());

        runnable.updatePriority();

        assertEquals(0.0, runnable.getPriority());
    }
}
