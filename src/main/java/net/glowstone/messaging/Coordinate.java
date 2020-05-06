package net.glowstone.messaging;

import java.util.Objects;

public final class Coordinate {

    public int x;
    public int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if ((!(other instanceof Coordinate))) {
            return false;
        }

        Coordinate that = (Coordinate) other;
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
