package science.atlarge.opencraft.opencraft.executor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * The executor that can run the ChunkRunnables. ChunkRunnables that are closed to the player are prioritized, since
 * they are the most relevant to the player.
 */
public final class PriorityExecutor<GenericPriorityRunnable extends PriorityRunnable> {

    private final ThreadPoolExecutor executor;
    private final SortableBlockingQueue<GenericPriorityRunnable> queue;

    /**
     * Create a PriorityExecutor that can run ChunkRunnables. The PriorityExecutor uses a thread pool executor
     * internally.
     *
     * @param poolSize The number of threads in the pool
     * @throws IllegalArgumentException if poolSize < 0
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public PriorityExecutor(int poolSize) throws IllegalArgumentException {
        queue = new SortableBlockingQueue<>(GenericPriorityRunnable::compareTo);
        BlockingQueue<Runnable> castedQueue = (BlockingQueue<Runnable>) ((BlockingQueue) queue);
        executor = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, castedQueue);
        executor.prestartAllCoreThreads();
    }

    /**
     * Create a PriorityExecutor that can run ChunkRunnables. The PriorityExecutor uses a thread pool executor
     * internally.
     */
    public PriorityExecutor() {
        this(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Execute the given runnables and remove the runnables that were previously enqueued and match the given predicate.
     *
     * @param toExecute The runnables to be executed.
     * @param predicate The predicate used to determine which runnables should be removed.
     * @return the removed runnables.
     */
    public Set<GenericPriorityRunnable> executeAndCancel(
            List<GenericPriorityRunnable> toExecute,
            Predicate<GenericPriorityRunnable> predicate
    ) {
        Set<GenericPriorityRunnable> cancelled = new HashSet<>();

        queue.transaction(queue -> {
            queue.forEach(GenericPriorityRunnable::updatePriority);
            queue.removeIf(runnable -> {
                if (predicate.test(runnable)) {
                    cancelled.add(runnable);
                    return true;
                }
                return false;
            });
            queue.addAll(toExecute);
            queue.sort();
        });

        return cancelled;
    }

    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be
     * accepted. Invocation has no additional effect if already shut down. This method does not wait for previously
     * submitted tasks to complete execution.
     */
    public void shutdown() {
        executor.shutdown();
    }
}
