package net.glowstone.util;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

/**
 * This class stores variables relevant for the player. These variables can be used to store old values to check for
 * differences between the old and new values and see if they have changed.
 */
public final class AreaOfInterest {

    @Setter
    private Location location;

    @Getter
    @Setter
    private int viewDistance;

    /**
     * Create an AreaOfInterest object.
     *
     * @param location The location to be stored.
     * @param viewDistance The viewdistance to be stored.
     */
    public AreaOfInterest(Location location, int viewDistance) {
        this.location = location;
        this.viewDistance = viewDistance;
    }

    /**
     * Getter for the location.
     *
     * @return A clone of the location.
     */
    public Location getLocation() {
        if (location == null) {
            return null;
        }
        return location.clone();
    }

    /**
     * Getter for the view distance.
     * @return The view distance.
     */
    public int getViewDistance() {
        return viewDistance;
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

        return viewDistance == that.viewDistance && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, viewDistance);
    }
}
