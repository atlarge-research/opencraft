package net.glowstone.executor;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.entity.GlowPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * The executor that can run the ChunkRunnables. ChunkRunnables that are closed to the player are prioritized, since
 * they are the most relevant to the player.
 */
public class PriorityExecutor {

    private final ThreadPoolExecutor executor;

    /**
     * Create a PriorityExecutor that can run ChunkRunnables. The PriorityExecutor uses a thread pool executor
     * internally.
     *
     * @param corePoolSize the number of threads to keep in the pool, even if they are idle, unless {@code
     *     allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the pool
     * @param keepAliveTime when the number of threads is greater than the core, this is the maximum time that
     *     excess idle threads will wait for new tasks before terminating.
     * @param unit the time unit for the {@code keepAliveTime} argument.
     * @throws IllegalArgumentException if one of the following holds:<br> {@code corePoolSize < 0}<br> {@code
     *     keepAliveTime < 0}<br> {@code maximumPoolSize <= 0}<br> {@code maximumPoolSize < corePoolSize}
     */
    public PriorityExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        BlockingQueue<Runnable> queue = new PriorityBlockingQueue<>();
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, queue);
    }

    /**
     * Create a PriorityExecutor that can run ChunkRunnables. The PriorityExecutor uses a thread pool executor
     * internally.
     */
    public PriorityExecutor() {
        this(1, Runtime.getRuntime().availableProcessors(), 60L, TimeUnit.SECONDS);
    }

    /**
     * Drain the executor queue to the given collection. Runnables that have been executed or are already being executed
     * will not be drained, since they are no longer in the queue.
     * <br><br>
     * Note that this method is NOT thread safe. Adding elements to the queue while this method is being executed could
     * result in messages being cleared that are not actually drained. In other words it might be possible that more
     * runnables are removed from the queue than the amount of runnables returned from this method.
     *
     * @param chunkRunnables The runnables that have been drained from the executor queue.
     */
    public void drainTo(Collection<ChunkRunnable> chunkRunnables) {
        // This cast is safe, because only ChunkRunnables are enqueued.
        BlockingQueue<ChunkRunnable> queue = ((BlockingQueue) executor.getQueue());
        queue.drainTo(chunkRunnables);
    }

    public void drainPlayerTo(final GlowPlayer player, Collection<ChunkRunnable> chunkRunnables) {
        BlockingQueue<Runnable> queue = executor.getQueue();

        //TODO: Check performance
        queue.removeIf(runnable -> {
            ChunkRunnable chunkRunnable = (ChunkRunnable) runnable;
            boolean toRemove = ((ChunkRunnable) runnable).hasEntityId(player.getEntityId());

            if (toRemove) {
                chunkRunnables.add(chunkRunnable);
            }

            return toRemove;
        });
    }

    /**
     * Create and execute a chunkrunnable on this priority executor.
     *
     * @param player The player that is used to determine to which player the ChunkRunnable belongs.
     * @param chunk The chunk that will be sent to the player.
     * @param command The runnable that will be executed.
     */
    public void execute(GlowPlayer player, GlowChunk chunk, @NotNull Runnable command) {
        ChunkRunnable chunkRunnable = new ChunkRunnable(player, chunk, command);
        execute(chunkRunnable);
    }

    /**
     * Execute the chunk runnable on this executor.
     *
     * @param chunkRunnable The chunk runnable that will be executed.
     */
    public void execute(ChunkRunnable chunkRunnable) {
        executor.execute(chunkRunnable);
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
