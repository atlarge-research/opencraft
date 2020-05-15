package net.glowstone.executor;

import java.util.Collection;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.util.Coordinates;
import org.jetbrains.annotations.NotNull;

public class PriorityExecutor {

    private final ThreadPoolExecutor executor;

    public PriorityExecutor() {
        executor = new ThreadPoolExecutor(0, Runtime.getRuntime().availableProcessors(), 60L, TimeUnit.SECONDS, new PriorityBlockingQueue<>());
    }

    @SuppressWarnings("unchecked")
    public void drainTo(Collection<ChunkRunnable> chunkRunnables) {
        // TODO: Mention removal is not thread safe
        // The only way runnables are added is through the execute method in this class. And this method always
        // wraps them in PriorityRunnables. So this cast is safe, as long as the execute method keeps upholding this
        // invariant.
        chunkRunnables.addAll((Collection<ChunkRunnable>) (Collection) executor.getQueue());
        executor.getQueue().clear();
    }

    public void execute(Coordinates playerCoords, int playerId, GlowChunk.Key chunkKey, @NotNull Runnable command) {
        ChunkRunnable chunkRunnable = new ChunkRunnable(playerCoords, playerId, chunkKey, command);
        executor.execute(chunkRunnable);
    }

    public void execute(ChunkRunnable chunkRunnable) {
        executor.execute(chunkRunnable);
    }
}
