package science.atlarge.opencraft.opencraft.chunk;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import science.atlarge.opencraft.opencraft.GlowWorld;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

/**
 * This class stores variables relevant for the player. These variables can be used to store old values to check for
 * differences between the old and new values and see if they have changed.
 */
public final class AreaOfInterest implements Iterable<GlowChunk> {

    private final GlowWorld world;
    private final int centerX;
    private final int centerZ;
    private final int radius;

    /**
     * Create an AreaOfInterest object.
     *
     * @param location The location to be stored.
     * @param viewDistance The viewdistance to be stored.
     */
    public AreaOfInterest(Location location, int viewDistance) {

        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }

        world = (GlowWorld) location.getWorld();
        centerX = location.getBlockX() >> 4;
        centerZ = location.getBlockZ() >> 4;
        Server server = world.getServer();
        radius = Math.min(viewDistance + 1, server.getViewDistance());
    }

    /**
     * Get the world in which the area resides.
     *
     * @return the world.
     */
    public GlowWorld getWorld() {
        return world;
    }

    /**
     * Get the x-coordinate of the center chunk of the area.
     *
     * @return the x-coordinate.
     */
    public int getCenterX() {
        return centerX;
    }

    /**
     * Get the z-coordinate of the center chunk of the area.
     *
     * @return the z-coordinate.
     */
    public int getCenterZ() {
        return centerZ;
    }

    /**
     * Get the radius of the area in chunks.
     *
     * @return the radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Check whether the given chunk is located within the area of interest.
     *
     * @param chunk the chunk to check.
     * @return whether the chunk is in the area.
     */
    public boolean contains(Chunk chunk) {
        return chunk.getWorld() == world
                && Math.abs(chunk.getX() - centerX) <= radius
                && Math.abs(chunk.getZ() - centerZ) <= radius;
    }

    @Override
    public void forEach(Consumer<? super GlowChunk> action) {
        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                GlowChunk chunk = world.getChunkAt(x, z);
                action.accept(chunk);
            }
        }
    }

    @NotNull
    @Override
    public Iterator<GlowChunk> iterator() {
        return new ChunkIterator(this);
    }

    @Override
    public Spliterator<GlowChunk> spliterator() {
        return new ChunkSpliterator(this);
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        AreaOfInterest that = (AreaOfInterest) other;

        return Objects.equals(world, that.world)
                && centerX == that.centerX
                && centerZ == that.centerZ
                && radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, centerX, centerZ, radius);
    }
}
