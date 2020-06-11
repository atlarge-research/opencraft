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

    private final Location location;
    private final int viewDistance;

    /**
     * Create an AreaOfInterest object.
     *
     * @param location The location to be stored.
     * @param viewDistance The viewdistance to be stored.
     */
    public AreaOfInterest(@NotNull Location location, int viewDistance) {
        this.location = location;
        this.viewDistance = viewDistance;
    }

    public World getWorld() {
        return location.getWorld();
    }

    public int getCenterX() {
        return location.getBlockX() >> 4;
    }

    public int getCenterZ() {
        return location.getBlockZ() >> 4;
    }

    public int getRadius(int limit) {
        return Math.min(viewDistance, limit);
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

        return Objects.equals(viewDistance, that.viewDistance) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, viewDistance);
    }
}
