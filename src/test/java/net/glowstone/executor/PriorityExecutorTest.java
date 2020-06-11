package net.glowstone.executor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.messaging.TimeBasedTest;
import net.glowstone.util.Coordinates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Test class that verifies whether the PriorityExecutor can get drained of not yet executed runnables and whether
 * runnables are executed in order based on their priority.
 */
class PriorityExecutorTest {

    private PriorityExecutor executor;
    private GlowChunk chunk;
    private GlowPlayer playerOrigin;
    private GlowPlayer player;
    private GlowPlayer playerFurther;

    /**
     * Setup the priority executor and the required mocks.
     */
    @BeforeEach
    void beforeEach() {

        executor = new PriorityExecutor(1);

        chunk = mock(GlowChunk.class);
        when(chunk.getCenterCoordinates()).thenReturn(new Coordinates(0, 0));

        playerOrigin = mock(GlowPlayer.class);
        when(playerOrigin.getCoordinates()).thenReturn(new Coordinates(0, 0));

        player = mock(GlowPlayer.class);
        when(player.getCoordinates()).thenReturn(new Coordinates(10, 20));

        playerFurther = mock(GlowPlayer.class);
        when(playerFurther.getCoordinates()).thenReturn(new Coordinates(100, 25));
    }

    /**
     * Shutdown the executor after each test is completed.
     */
    @AfterEach
    void afterEach() {
        executor.shutdown();
    }

    /**
     * Verify that elements are executed in the correct order and properly cancelled.
     *
     * @throws ExecutionException Whenever the future is completed exceptionally.
     * @throws InterruptedException Whenever the future's getter is interrupted.
     * @throws TimeoutException Whenever the timeout is reached on the future's getter.
     */
    @TimeBasedTest
    void executeAndCancel() throws ExecutionException, InterruptedException, TimeoutException {

        CountDownLatch latch = new CountDownLatch(1);
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        ChunkRunnable blocking = new ChunkRunnable(playerOrigin, chunk, () -> {
            try {
                latch.await();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });

        ChunkRunnable toBeCancelled = new ChunkRunnable(player, chunk, () -> future.complete(false));

        executor.executeAndCancel(Arrays.asList(blocking, toBeCancelled), runnable -> false);

        ChunkRunnable toBeExecuted = new ChunkRunnable(playerFurther, chunk, () -> future.complete(true));

        Collection<ChunkRunnable> notCancelled = executor.executeAndCancel(new ArrayList<>(), runnable -> false);
        assertTrue(notCancelled.isEmpty());

        Collection<ChunkRunnable> cancelled = executor.executeAndCancel(
                Collections.singleton(toBeExecuted),
                runnable -> runnable.getPlayer() == player
        );

        latch.countDown();

        assertTrue(future.get(50L, TimeUnit.MILLISECONDS));
        assertTrue(cancelled.contains(toBeCancelled));
    }
}
