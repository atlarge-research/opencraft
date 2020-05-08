package net.glowstone.util;

import java.util.Objects;
import org.bukkit.util.NumberConversions;

/**
 * This class represents a 3d location.
 */
public class Coordinates {
    private double x;
    private double z;

    /**
     * Construct a coordinates object from three separate coordinates.
     *
     * @param x The x coordinate.
     * @param z The z coordinate.
     */
    public Coordinates(double x, double z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Get the x coordinate.
     *
     * @return The x coordinate at this location.
     */
    public double getX() {
        return x;
    }

    /**
     * Set the x coordinate to a new value.
     *
     * @param x The new x coordinate value.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Get the z coordinate.
     *
     * @return The z coordinate at this location.
     */
    public double getZ() {
        return z;
    }

    /**
     * Set the z coordinate to a new value.
     *
     * @param z The new z coordinate value.
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Get the x coordinate as integer.
     *
     * @return The x coordinate as integer at this location.
     */
    public int getBlockX() {
        return convertToBlock(getX());
    }

    /**
     * Get the z coordinate as integer.
     *
     * @return The z coordinate as integer at this location.
     */
    public int getBlockZ() {
        return convertToBlock(getZ());
    }

    /**
     * Get the x coordinate of the chunk the coordinates position is located in.
     *
     * @return The x coordinate of the associated chunk.
     */
    public double getChunkX() {
        // Divide by 16 (chunk size)
        return getBlockX() >> 4;
    }

    /**
     * Get the z coordinate of the chunk the coordinates position is located in.
     *
     * @return The z coordinate of the associated chunk.
     */
    public double getChunkZ() {
        // Divide by 16 (chunk size)
        return getBlockZ() >> 4;
    }

    /**
     * Calculate the squared distance between these coordinates and the given coordinates.
     *
     * @param coordinates The coordinates to which the squared distance is computed.
     * @return The squared distance between these coordinates and the given coordinates.
     */
    public double squaredDistance(Coordinates coordinates) {
        return NumberConversions.square(this.x - coordinates.x) + NumberConversions.square(this.z - coordinates.z);
    }

    /**
     * Calculate the distance between these coordinates and the given coordinates.
     *
     * @param coordinates The coordinates to which the distance is computed.
     * @return The distance between these coordinates and the given coordinates.
     */
    public double distance(Coordinates coordinates) {
        return Math.sqrt(squaredDistance(coordinates));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coordinates that = (Coordinates) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    public String toString() {
        return "Coordinates{x=" + this.x + ",z=" + this.z + "}";
    }

    /**
     * Retrieve the location of the block as integer.
     *
     * @param coordinate The location of the coordinate that needs to be converted to integer form.
     * @return The floored double as integer.
     */
    private static int convertToBlock(double coordinate) {
        return NumberConversions.floor(coordinate);
    }
}
