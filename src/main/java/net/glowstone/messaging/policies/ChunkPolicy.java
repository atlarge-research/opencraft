package net.glowstone.messaging.policies;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import science.atlarge.opencraft.messaging.Policy;

/**
 * Defines a mapping from players to chunks of interest and from objects to the chunk in which they reside.
 */
public final class ChunkPolicy implements Policy<Chunk, Object, Player> {

    private final World world;
    private final int viewDistance;

    /**
     * Create a chunk policy for the given world and view distance.
     *
     * @param world the world from which chunks may be interesting.
     * @param viewDistance the maximum view distance of any player.
     */
    public ChunkPolicy(World world, int viewDistance) {
        this.world = world;
        this.viewDistance = viewDistance;
    }

    @Override
    public Set<Chunk> computeInterestSet(Player player) {

        Location location = player.getLocation();
        if (location.getWorld() != world) {
            return new HashSet<>();
        }

        int centerX = location.getBlockX() >> 4;
        int centerZ = location.getBlockZ() >> 4;
        int radius = Math.min(viewDistance, player.getViewDistance());

        Set<Chunk> chunks = new HashSet<>();
        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                Chunk chunk = world.getChunkAt(x, z);
                chunks.add(chunk);
            }
        }
        return chunks;
    }

    @Override
    public Iterable<Chunk> selectTargets(Object publisher) {

        if (publisher instanceof Chunk) {
            Chunk chunk = (Chunk) publisher;
            return Collections.singletonList(chunk);
        }

        if (publisher instanceof Block) {
            Chunk chunk = ((Block) publisher).getChunk();
            return Collections.singletonList(chunk);
        }

        if (publisher instanceof Entity) {
            Chunk chunk = ((Entity) publisher).getChunk();
            return Collections.singletonList(chunk);
        }

        if (publisher instanceof Location) {
            Chunk chunk = ((Location) publisher).getChunk();
            return Collections.singletonList(chunk);
        }

        throw new UnsupportedOperationException("Cannot select target topic for type: " + publisher.getClass());
    }
}
