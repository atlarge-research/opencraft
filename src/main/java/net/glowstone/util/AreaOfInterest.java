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

    @Getter
    @Setter
    private Location location;

    @Getter
    @Setter
    private int viewDistance;

    /**
     * Create an AreaOfInterest object.
     * @param location The location to be stored.
     * @param viewDistance The viewdistance to be stored.
     */
    public AreaOfInterest(Location location, int viewDistance) {
        this.location = location;
        this.viewDistance = viewDistance;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AreaOfInterest that = (AreaOfInterest) o;

        return viewDistance == that.viewDistance && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, viewDistance);
    }
}
