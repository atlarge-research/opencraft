package net.glowstone.executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
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

    private PriorityExecutor priorityExecutor;
    private GlowChunk chunk;
    private GlowPlayer player;
    private GlowPlayer playerFurther;
    private GlowPlayer playerOrigin;

    /**
     * Create the PriorityExecutor and the mock objects needed for testing the executor.
     */
    @BeforeEach
    void setUp() {
        priorityExecutor = new PriorityExecutor(1, 1, 60L, TimeUnit.SECONDS);

        chunk = mock(GlowChunk.class);
        when(chunk.getCenterCoordinates()).thenReturn(new Coordinates(-1, 1));

        player = mock(GlowPlayer.class);
        Coordinates playerCoords = new Coordinates(10, 20);
        when(player.getCoordinates()).thenReturn(playerCoords);

        playerFurther = mock(GlowPlayer.class);
        Coordinates playerCoordsFurther = new Coordinates(100, 25);
        when(playerFurther.getCoordinates()).thenReturn(playerCoordsFurther);

        playerOrigin = mock(GlowPlayer.class);
        Coordinates origin = new Coordinates(0, 0);
        when(playerOrigin.getCoordinates()).thenReturn(origin);
    }

    /**
     * Shutdown the executor after each test is completed.
     */
    @AfterEach
    void tearDown() {
        priorityExecutor.shutdown();
    }

    /**
     * Verify that all the runnables in the queue are drained properly.
     *
     * @throws ExecutionException if the future completed with an exception.
     * @throws InterruptedException if the thread is interrupted while waiting.
     * @throws TimeoutException if the future did not complete before it timed out.
     */
    @TimeBasedTest
    void drainTo() throws ExecutionException, InterruptedException, TimeoutException {
        AtomicReference<InterruptedException> atomicException = new AtomicReference<>();
        AtomicBoolean executed = new AtomicBoolean(false);
        CompletableFuture<Boolean> executedFurther = new CompletableFuture<>();
        CountDownLatch awaitQueued = new CountDownLatch(1);

        priorityExecutor.execute(playerOrigin, chunk, () -> {
            try {
                awaitQueued.await();
            } catch (InterruptedException exception) {
                atomicException.set(exception);
            }
        });

        priorityExecutor.execute(player, chunk, () -> executed.set(true));

        List<ChunkRunnable> drained = new ArrayList<>();
        priorityExecutor.drainTo(drained);
        awaitQueued.countDown();

        priorityExecutor.execute(playerFurther, chunk, () -> executedFurther.complete(true));

        assertEquals(1, drained.size());
        assertTrue(executedFurther.get(50L, TimeUnit.MILLISECONDS));
        assertFalse(executed.get());
    }

    /**
     * Verify that the runnables are executed in order based on their priority.
     *
     * @throws InterruptedException if the thread is interrupted while waiting for the countdown latch to complete.
     */
    @TimeBasedTest
    void execute() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        AtomicReference<InterruptedException> atomicException = new AtomicReference<>();
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());

        CountDownLatch awaitQueued = new CountDownLatch(1);

        priorityExecutor.execute(playerOrigin, chunk, () -> {
            try {
                awaitQueued.await();
            } catch (InterruptedException exception) {
                atomicException.set(exception);
            } finally {
                latch.countDown();
            }
        });

        priorityExecutor.execute(playerFurther, chunk, () -> {
            list.add(2);
            latch.countDown();
        });

        priorityExecutor.execute(player, chunk, () -> {
            list.add(1);
            latch.countDown();
        });

        awaitQueued.countDown();

        List<Integer> expectedList = Arrays.asList(1, 2);

        assertTrue(latch.await(50L, TimeUnit.MILLISECONDS));
        assertNull(atomicException.get());
        assertEquals(expectedList, list);
    }
}