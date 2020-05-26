package net.glowstone.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * The executor that can run the ChunkRunnables. ChunkRunnables that are closed to the player are prioritized, since
 * they are the most relevant to the player.
 */
public final class PriorityExecutor {

    private final ThreadPoolExecutor executor;
    private final SortableBlockingQueue<ChunkRunnable> queue;

    /**
     * Create a PriorityExecutor that can run ChunkRunnables. The PriorityExecutor uses a thread pool executor
     * internally.
     *
     * @param poolSize The number of threads in the pool
     * @throws IllegalArgumentException if poolSize < 0
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public PriorityExecutor(int poolSize) {
        queue = new SortableBlockingQueue<>(ChunkRunnable::compareTo);
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
    public Collection<ChunkRunnable> executeAndCancel(
            Collection<ChunkRunnable> toExecute,
            Predicate<ChunkRunnable> predicate
    ) {
        Collection<ChunkRunnable> removed = new ArrayList<>();

        queue.transaction(queue -> {
            queue.forEach(ChunkRunnable::updatePriority);
            queue.linearRemoveIf(predicate, removed);
            queue.addAll(toExecute);
            queue.sort();
        });

        return removed;
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
