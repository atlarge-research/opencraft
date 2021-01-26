package science.atlarge.opencraft.opencraft.util;

import org.bukkit.util.Vector;

/**
 * This class adds utility functions for the bukkit vector class.
 */
public class Vectors {

    /**
     * A private Vectors constructor that ensures that this class is will only be used for static methods.
     */
    private Vectors(){}

    /**
     * Floors every parameter in the vector.
     *
     * @param vector the vector that will be floored
     * @return A new floored vector
     */
    public static Vector floor(Vector vector) {
        return new Vector(
                Math.floor(vector.getX()),
                Math.floor(vector.getY()),
                Math.floor(vector.getZ())
        );
    }

    /**
     * Ceils every parameter in the vector.
     *
     * @param vector the vector that will be ceiled
     * @return A new ceiled vector
     */
    public static Vector ceil(Vector vector) {
        return new Vector(
                Math.ceil(vector.getX()),
                Math.ceil(vector.getY()),
                Math.ceil(vector.getZ())
        );
    }

    /**
     * Projects a vector onto another vector.
     *
     * @param vector The vector that will be projected
     * @param normal The vector on which the projection will occur
     * @return A projected vector
     */
    public static Vector project(Vector vector, Vector normal) {
        double dot = vector.dot(normal);
        return normal.clone().multiply(dot);
    }

    /**
     * Clamp a vector within some magnitude.
     *
     * @param vector The vector that will be clamped
     * @param magnitude The value that will be used a clamping threshold
     * @return A projected vector
     */
    public static Vector clamp(Vector vector, double magnitude) {
        double length = vector.length();
        if  (length > magnitude) {
            return vector.clone().multiply(magnitude / length);
        }
        return vector.clone();
    }

    /**
     * An equals method that allows matching two vectors per parameter with a certain tolerance.
     *
     * @param vector one of the vectors to compare
     * @param otherVector one of the vectors to compare
     * @param tolerance an allowed tolerance
     * @return A boolean that is true if the vectors are equal within tolerance
     */
    public static boolean equals(Vector vector, Vector otherVector, double tolerance) {

        double dx = Math.abs(vector.getX() - otherVector.getX());

        if (dx >= tolerance) {
            return false;
        }

        double dy = Math.abs(vector.getY() - otherVector.getY());

        if (dy >= tolerance) {
            return false;
        }

        double dz = Math.abs(vector.getZ() - otherVector.getZ());

        if (dz >= tolerance) {
            return false;
        }

        return true;
    }

    /**
     * An equals method that allows matching two vectors per parameter with a certain tolerance prefilled.
     *
     * @param vector one of the vectors to compare
     * @param otherVector one of the vectors to compare
     * @return A boolean that is true if the vectors are equal within tolerance
     */
    public static boolean equals(Vector vector, Vector otherVector) {
        return equals(vector, otherVector, Double.MIN_VALUE);
    }

    /**
     * Computes the volume of the given vector by multiplying all axises of the vector with each other.
     *
     * @param diagonal The vector that stretches diagonally over the volume to compute
     * @return A double indicating the volume
     */
    public static double computeVolume(Vector diagonal) {
        double x = Math.abs(diagonal.getX());
        double y = Math.abs(diagonal.getY());
        double z = Math.abs(diagonal.getZ());
        return x * y * z;
    }

    /**
     * Checks every axis on the vector for the INFINITY value and flip it if found.
     *
     * @param vector The vector to flip the INFINITY values of
     */
    public static void flipInfinity(Vector vector) {

        if (Double.isInfinite(vector.getX())) {
            vector.setX(vector.getX() * -1);
        }

        if (Double.isInfinite(vector.getY())) {
            vector.setY(vector.getY() * -1);
        }

        if (Double.isInfinite(vector.getZ())) {
            vector.setZ(vector.getZ() * -1);
        }
    }
}
