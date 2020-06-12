package net.glowstone.util;

import java.util.Objects;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * This class stores variables relevant for the player. These variables can be used to store old values to check for
 * differences between the old and new values and see if they have changed.
 */
public final class AreaOfInterest {

    private final int x;
    private final int z;
    private final World world;
    private final int viewDistance;

    /**
     * Create an AreaOfInterest object.
     *
     * @param location The location to be stored.
     * @param viewDistance The viewdistance to be stored.
     */
    public AreaOfInterest(@NotNull Location location, int viewDistance) {
        x = location.getBlockX() >> 4;
        z = location.getBlockZ() >> 4;
        world = location.getWorld();
        this.viewDistance = viewDistance;
    }

    public int getCenterX() {
        return x;
    }

    public int getCenterZ() {
        return z;
    }

    public World getWorld() {
        return world;
    }

    public int getRadius(int limit) {
        return Math.min(viewDistance + 1, limit);
    }

    public boolean contains(Chunk chunk, int limit) {
        int radius = getRadius(limit);
        return chunk.getWorld() == getWorld()
                && Math.abs(chunk.getX() - getCenterX()) <= radius
                && Math.abs(chunk.getZ() - getCenterZ()) <= radius;
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

        return x == that.x
                && z == that.z
                && Objects.equals(world, that.world)
                && Objects.equals(viewDistance, that.viewDistance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z, world, viewDistance);
    }
}
