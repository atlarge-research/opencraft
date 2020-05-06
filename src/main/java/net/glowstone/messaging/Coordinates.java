package net.glowstone.messaging;

import java.util.Objects;

/**
 * The coordinates class represents a position in 2-dimensional space.
 */
public class Coordinates {

    public int x;
    public int y;

    /**
     * Combines two coordinates into a single object.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if ((!(other instanceof Coordinates))) {
            return false;
        }

        Coordinates that = (Coordinates) other;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
