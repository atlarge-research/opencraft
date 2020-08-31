package net.glowstone.executor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class that verifies whether the PriorityExecutor can get drained of not yet executed runnables and whether
 * runnables are executed in order based on their priority.
 */
class PriorityExecutorTest {

    private PriorityExecutor<TestPriorityRunnable> executor;

    /**
     * Setup the priority executor and the required mocks.
     */
    @BeforeEach
    void beforeEach() {
        executor = new PriorityExecutor<>(1);
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
     * @throws ExecutionException   Whenever the future is completed exceptionally.
     * @throws InterruptedException Whenever the future's getter is interrupted.
     * @throws TimeoutException     Whenever the timeout is reached on the future's getter.
     */
    @Test
    void executeAndCancel() throws ExecutionException, InterruptedException, TimeoutException {

        final double highPriority = 0.0;
        final double mediumPriority = 10.0;
        final double lowPriority = 120.0;

        CountDownLatch latch = new CountDownLatch(1);
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        TestPriorityRunnable blocking = new TestPriorityRunnable(highPriority, () -> {
            try {
                latch.await();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });

        TestPriorityRunnable toBeCancelled = new TestPriorityRunnable(mediumPriority, () -> future.complete(false));

        executor.executeAndCancel(Arrays.asList(blocking, toBeCancelled), runnable -> false);

        TestPriorityRunnable toBeExecuted = new TestPriorityRunnable(lowPriority, () -> future.complete(true));

        Collection<TestPriorityRunnable> notCancelled = executor.executeAndCancel(new ArrayList<>(), runnable -> false);
        assertTrue(notCancelled.isEmpty());

        Collection<TestPriorityRunnable> cancelled =
            executor.executeAndCancel(Collections.singletonList(toBeExecuted), runnable -> runnable == toBeCancelled);

        latch.countDown();

        assertTrue(future.get(50L, TimeUnit.MILLISECONDS));
        assertTrue(cancelled.contains(toBeCancelled));
    }

    private static final class TestPriorityRunnable extends PriorityRunnable {

        private final Runnable runnable;

        public TestPriorityRunnable(double priority, Runnable runnable) {
            setPriority(priority);
            this.runnable = runnable;
        }

        @Override
        public void updatePriority() {
            // not relevant for these tests
        }

        @Override
        public void run() {
            runnable.run();
        }
    }
}
