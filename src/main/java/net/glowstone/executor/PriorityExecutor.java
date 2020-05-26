package net.glowstone.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import net.glowstone.util.SortableBlockingQueue;

/**
 * The executor that can run the ChunkRunnables. ChunkRunnables that are closed to the player are prioritized, since
 * they are the most relevant to the player.
 */
public class PriorityExecutor {

    private final ThreadPoolExecutor executor;
    private final SortableBlockingQueue<ChunkRunnable> sortableBlockingQueue;

    /**
     * Create a PriorityExecutor that can run ChunkRunnables. The PriorityExecutor uses a thread pool executor
     * internally.
     *
     * @param poolSize the number of threads in the pool
     * @throws IllegalArgumentException if poolSize < 0
     */
    public PriorityExecutor(int poolSize) {
        sortableBlockingQueue = new SortableBlockingQueue<>(new ChunkRunnableComparator());
        BlockingQueue<Runnable> queue = (BlockingQueue<Runnable>) ((BlockingQueue) sortableBlockingQueue);
        executor = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, queue);
        executor.prestartAllCoreThreads();
    }

    /**
     * Create a PriorityExecutor that can run ChunkRunnables. The PriorityExecutor uses a thread pool executor
     * internally.
     */
    public PriorityExecutor() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public Collection<ChunkRunnable> executeAndCancel(
            Collection<ChunkRunnable> toAdd,
            Predicate<ChunkRunnable> predicate
    ) {

        Collection<ChunkRunnable> removed = new ArrayList<>();

        sortableBlockingQueue.transaction(queue -> {
            queue.forEach(ChunkRunnable::updatePriority);
            queue.linearRemoveIf(predicate, removed);
            queue.addAll(toAdd);
            queue.sort();
        });

        return removed;
    }

//    /**
//     * Create and execute a chunkrunnable on this priority executor.
//     *
//     * @param player The player that is used to determine to which player the ChunkRunnable belongs.
//     * @param chunk The chunk that will be sent to the player.
//     * @param command The runnable that will be executed.
//     */
//    public void execute(GlowPlayer player, GlowChunk chunk, @NotNull Runnable command) {
//        ChunkRunnable chunkRunnable = new ChunkRunnable(player, chunk, command);
//        execute(chunkRunnable);
//    }
//
//    /**
//     * Execute the chunk runnable on this executor.
//     *
//     * @param chunkRunnable The chunk runnable that will be executed.
//     */
//    public void execute(ChunkRunnable chunkRunnable) {
//        executor.execute(chunkRunnable);
//    }

    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be
     * accepted. Invocation has no additional effect if already shut down. This method does not wait for previously
     * submitted tasks to complete execution.
     */
    public void shutdown() {
        executor.shutdown();
    }
//
//    private static final class DynamicPriorityExecutor extends ThreadPoolExecutor {
//
//        public DynamicPriorityExecutor(
//                int poolSize,
//                long keepAliveTime,
//                TimeUnit unit,
//                BlockingQueue<Runnable> workQueue
//        ) {
//            super(poolSize, poolSize, keepAliveTime, unit, workQueue);
//        }
//
//        public void
//    }
}
