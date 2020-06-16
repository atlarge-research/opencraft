package net.glowstone.executor;

import com.flowpowered.network.Message;
import java.util.Objects;
import net.glowstone.GlowWorld;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.GlowSession;
import net.glowstone.chunk.AreaOfInterest;
import net.glowstone.util.Coordinates;
import org.bukkit.World;

/**
 * This class is class responsible for generating chunks and sending the chunk data to a player. This class has a
 * priority that is used for executing ChunkRunnables in a priority based order. The order is normally determined by the
 * distance between the chunk and the player. So chunks closer to the player will be prioritized over chunks further
 * away from the player.
 */
public final class ChunkRunnable extends PriorityRunnable {

    private final GlowPlayer player;
    private final GlowChunk chunk;

    /**
     * Construct a ChunkRunnable for a chunk that the given player needs to receive the data for.
     *
     * @param player The player to which the chunk data will be sent to.
     * @param chunk The chunk for which the data needs to be sent to the player.
     */
    public ChunkRunnable(GlowPlayer player, GlowChunk chunk) {
        this.player = player;
        this.chunk = chunk;
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
     * Check whether the chunk runnable should be cancelled. The decision is made based on the distance between the
     * server's view distance, the player's view distance and position, and the chunk's position.
     *
     * @return whether the runnable should be cancelled.
     */
    public boolean shouldBeCancelled() {
        AreaOfInterest area = player.getAreaOfInterest();
        return !area.contains(chunk);
    }

    /**
     * Update the priority of the ChunkRunnable. This is computed by calculating the distance between the center of the
     * chunk the player.
     */
    @Override
    public void updatePriority() {
        Coordinates chunkCenter = chunk.getCenterCoordinates();
        Coordinates playerCoordinates = player.getCoordinates();
        double squaredDistance = chunkCenter.squaredDistance(playerCoordinates);
        setPriority(squaredDistance);
    }

    @Override
    public void run() {

        GlowSession session = player.getSession();
        GlowWorld world = chunk.getWorld();
        int x = chunk.getX();
        int z = chunk.getZ();

        boolean skylight = world.getEnvironment() == World.Environment.NORMAL;

        world.getChunkManager().forcePopulation(x, z);

        GlowChunk.Key key = GlowChunk.Key.of(x, z);
        player.getChunkLock().acquire(key);

        Message message = chunk.toMessage(skylight);
        session.send(message);

        chunk.getRawBlockEntities().forEach(entity -> entity.update(player));
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        ChunkRunnable other = (ChunkRunnable) object;
        return Objects.equals(player, other.player) && Objects.equals(chunk, other.chunk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, chunk);
    }
}
