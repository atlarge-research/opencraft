package net.glowstone.util;

import org.bukkit.util.NumberConversions;

/**
 * This class represents a 3d location.
 */
public class Coordinates {
    private double x;
    private double y;
    private double z;

    /**
     * Construct a coordinates object from three separate coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     */
    public Coordinates(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Retrieve the location of the block as integer.
     *
     * @param coordinate The location of the coordinate that needs to be converted to integer form.
     * @return The floored double as integer.
     */
    public static int getBlockInteger(double coordinate) {
        return NumberConversions.floor(coordinate);
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
     * Get the y coordinate.
     *
     * @return The y coordinate at this location.
     */
    public double getY() {
        return y;
    }

    /**
     * Set the y coordinate to a new value.
     *
     * @param y The new y coordinate value.
     */
    public void setY(double y) {
        this.y = y;
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
        return getBlockInteger(getX());
    }

    /**
     * Get the y coordinate as integer.
     *
     * @return The y coordinate as integer at this location.
     */
    public int getBlockY() {
        return getBlockInteger(getY());
    }

    /**
     * Get the z coordinate as integer.
     *
     * @return The z coordinate as integer at this location.
     */
    public int getBlockZ() {
        return getBlockInteger(getZ());
    }

    /**
     * Get the x coordinate of the chunk the coordinates position is located in.
     *
     * @return The x coordinate of the associated chunk.
     */
    public double getChunkX() {
        return getBlockX() >> 4;
    }

    /**
     * Get the z coordinate of the chunk the coordinates position is located in.
     *
     * @return The z coordinate of the associated chunk.
     */
    public double getChunkZ() {
        return getBlockZ() >> 4;
    }

    /**
     * Add the given coordinates to the coordinates of this object.
     *
     * @param coordinates The coordinates that need to be added.
     * @return The updated coordinates. No new object will be created.
     */
    public Coordinates add(Coordinates coordinates) {
        this.x += coordinates.x;
        this.y += coordinates.y;
        this.z += coordinates.z;
        return this;
    }

    /**
     * Add the given separate coordinates to the coordinates of this object.
     *
     * @param x The x coordinate that will be added.
     * @param y The y coordinate that will be added.
     * @param z The z coordinate that will be added.
     * @return The updated coordinates. No new object will be created.
     */
    public Coordinates add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Subtract the given coordinates to the coordinates of this object.
     *
     * @param coordinates The coordinates that need to be subtracted.
     * @return The updated coordinates. No new object will be created.
     */
    public Coordinates subtract(Coordinates coordinates) {
        this.x -= coordinates.x;
        this.y -= coordinates.y;
        this.z -= coordinates.z;
        return this;
    }

    /**
     * Subtract the given separate coordinates to the coordinates of this object.
     *
     * @param x The x coordinate that will be added.
     * @param y The y coordinate that will be added.
     * @param z The z coordinate that will be added.
     * @return The updated coordinates. No new object will be created.
     */
    public Coordinates subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    /**
     * Calculate the length of the coordinate vector. This is the same as the distance between these coordinates and
     * the origin.
     *
     * @return The length of the coordinate vector.
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Calculate the squared length of the coordinate vector. This is the same as the squared distance between these
     * coordinates and the origin.
     *
     * @return The squared length of the coordinate vector.
     */
    public double lengthSquared() {
        return NumberConversions.square(this.x) + NumberConversions.square(this.y) + NumberConversions.square(this.z);
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

    /**
     * Calculate the squared distance between these coordinates and the given coordinates.
     *
     * @param coordinates The coordinates to which the squared distance is computed.
     * @return The squared distance between these coordinates and the given coordinates.
     */
    public double squaredDistance(Coordinates coordinates) {
        return NumberConversions.square(this.x - coordinates.x) + NumberConversions.square(this.y - coordinates.y)
            + NumberConversions.square(this.z - coordinates.z);
    }
}
