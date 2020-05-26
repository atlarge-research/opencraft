package net.glowstone.executor;

import net.glowstone.chunk.GlowChunk;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.util.Coordinates;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * The class responsible for sending the chunk data to a player. This class is a runnable that can be executed by an
 * executor. This class also has support for executing these ChunkRunnables in a priority based order. The order is
 * normally determined by the distance between the chunk and the player. So chunks closer to the player will be
 * prioritized over chunks further away from the player.
 */
public final class ChunkRunnable implements Runnable, Comparable<ChunkRunnable> {

    private final GlowPlayer player;
    private final GlowChunk chunk;
    private final Runnable runnable;
    private double priority;

    /**
     * Construct a ChunkRunnable for a chunk that the given player needs to receive the data for.
     *
     * @param player The player to which the chunk data will be sent to.
     * @param chunk The chunk for which the data needs to be sent to the player.
     * @param runnable The runnable that will be executed when this ChunkRunnable is executed.
     */
    public ChunkRunnable(GlowPlayer player, GlowChunk chunk, Runnable runnable) {
        this.player = player;
        this.chunk = chunk;
        this.runnable = runnable;
        updatePriority();
    }

    /**
     * Get the chunk for which the data needs to be sent to the player.
     *
     * @return The chunk for which the data needs to be sent to the player.
     */
    public GlowChunk getChunk() {
        return chunk;
    }

    /**
     * Get the player whom should receive the chunk data.
     *
     * @return The player whom should receive the chunk data.
     */
    public GlowPlayer getPlayer() {
        return player;
    }

    /**
     * Get both the chunk for which the data needs to be sent and the player whom should receive the chunk data.
     *
     * @return a pair of the chunk and player.
     */
    public Pair<GlowChunk, GlowPlayer> getChunkAndPlayer() {
        return Pair.of(chunk, player);
    }

    /**
     * Update the priority of the ChunkRunnable. This is computed by calculating the distance between the center of the
     * chunk the player.
     */
    public void updatePriority() {
        Coordinates chunkCenter = chunk.getCenterCoordinates();
        Coordinates playerCoords = player.getCoordinates();
        this.priority = chunkCenter.squaredDistance(playerCoords);
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
