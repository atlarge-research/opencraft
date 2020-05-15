package net.glowstone.executor;

import net.glowstone.chunk.GlowChunk;
import net.glowstone.util.Coordinates;
import org.jetbrains.annotations.NotNull;

public final class ChunkRunnable implements Runnable, Comparable<ChunkRunnable> {

    private final Runnable runnable;
    private final int playerId;
    private final GlowChunk.Key chunkKey;
    private double priority;

    public ChunkRunnable(Coordinates playerCoords, int playerId, GlowChunk.Key chunkKey, Runnable runnable) {
        this.playerId = playerId;
        this.chunkKey = chunkKey;
        this.runnable = runnable;

        updatePriority(playerCoords);
    }

    public int getPlayerId() {
        return playerId;
    }

    public GlowChunk.Key getChunkKey() {
        return chunkKey;
    }

    public void updatePriority(Coordinates coords) {
        Coordinates chunkCenter = Coordinates.createAtChunkCenter(chunkKey.getX(), chunkKey.getZ());

        this.priority = chunkCenter.squaredDistance(coords);
    }

    @Override
    public void run() {
        runnable.run();
    }

    @Override
    public int compareTo(@NotNull ChunkRunnable other) {
        return Double.compare(priority, other.priority);
    }
}
