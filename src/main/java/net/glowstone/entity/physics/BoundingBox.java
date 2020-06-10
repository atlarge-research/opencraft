package net.glowstone.entity.physics;

import org.bukkit.util.Vector;

/**
 * A rectangular bounding box with minimum and maximum corners.
 */
public class BoundingBox implements Cloneable {

    public static class Dimensions {
        
        public final double width;
        public final double height;

        public Dimensions(double width, double height) {
            this.width = width;
            this.height = height;
        }

        public static Dimensions create(double width, double height) {
            return new Dimensions(width, height);
        }
    }

    public final Vector minCorner = new Vector();
    public final Vector maxCorner = new Vector();

    /**
     * Tests whether this intersects another bounding box.
     * @param other another bounding box
     * @return true if this bounding box and {@code other} intersect; false otherwise
     */
    public final boolean intersects(BoundingBox other) {
        return intersects(this, other);
    }

    /**
     * Tests whether two bounding boxes intersect.
     *
     * @param a a bounding box
     * @param b a bounding box
     * @param tolerance a double that functions as allowed tolerance between blocks before they are deemed to intersect
     * @return true if {@code a} and {@code b} intersect; false otherwise
     */
    public static boolean intersects(BoundingBox a, BoundingBox b, double tolerance) {
        Vector minA = a.minCorner;
        Vector maxA = a.maxCorner;
        Vector minB = b.minCorner;
        Vector maxB = b.maxCorner;

        if (maxA.getX() + tolerance < minB.getX() || minA.getX() - tolerance > maxB.getX()) {
            return false;
        }

        if (maxA.getY() + tolerance < minB.getY() || minA.getY() - tolerance > maxB.getY()) {
            return false;
        }

        if (maxA.getZ() + tolerance < minB.getZ() || minA.getZ() - tolerance > maxB.getZ()) {
            return false;
        }

        return true;
    }

    /**
     * An overloaded variant of the intersects function that includes a predefined tolerance.
     * @param a a bounding box
     * @param b a bounding box
     * @return true if {@code a} and {@code b} intersect; false otherwise
     */
    public static boolean intersects(BoundingBox a, BoundingBox b) {
        return intersects(a, b, Double.MIN_VALUE);
    }

    /**
     * Converts two Vector instances to a BoundingBox.
     * @param a any corner
     * @param b the corner opposite {@code a}
     * @return a bounding box from {@code a} to {@code b}
     */
    public static BoundingBox fromCorners(Vector a, Vector b) {
        BoundingBox box = new BoundingBox();
        box.minCorner.setX(Math.min(a.getX(), b.getX()));
        box.minCorner.setY(Math.min(a.getY(), b.getY()));
        box.minCorner.setZ(Math.min(a.getZ(), b.getZ()));
        box.maxCorner.setX(Math.max(a.getX(), b.getX()));
        box.maxCorner.setY(Math.max(a.getY(), b.getY()));
        box.maxCorner.setZ(Math.max(a.getZ(), b.getZ()));
        return box;
    }

    /**
     * Creates a bounding box given its minimum corner and its size.
     * @param pos the minimum corner
     * @param size the displacement of the maximum corner from the minimum corner
     * @return a bounding box from {@code pos} to {@code pos.clone().add(size)}
     */
    public static BoundingBox fromPositionAndSize(Vector pos, Vector size) {
        BoundingBox box = new BoundingBox();
        box.minCorner.copy(pos);
        box.maxCorner.copy(pos.clone().add(size));
        return box;
    }

    /**
     * Creates a bounding box that is centered just as far from the minimum corner as from the maximum corner.
     * @param pos The position to start from
     * @param dimension The dimensions of the box
     * @return The bounding box
     */
    public static BoundingBox fromDimension(Vector pos, Dimensions dimension) {
        return fromCenterAndSize(pos, dimension.width, dimension.height);
    }


    /**
     * Creates a bounding box that is centered just as far from the minimum corner as from the maximum corner.
     * @param pos The position to start from
     * @param xzSize The x and z axis size
     * @param ySize The height of the bounding box
     * @return The bounding box
     */
    public static BoundingBox fromCenterAndSize(Vector pos, double xzSize, double ySize) {
        BoundingBox box = new BoundingBox();
        box.minCorner.copy(pos.clone().add(new Vector(0.5 - xzSize / 2.0, 0.0, 0.5 - xzSize / 2.0)));
        box.maxCorner.copy(pos.clone().add(new Vector(0.5 + xzSize / 2.0, ySize, 0.5 + xzSize / 2.0)));
        return box;
    }

    /**
     * Returns a deep copy of a BoundingBox.
     * @param original the BoundingBox to copy
     * @return a copy of {@code original}
     */
    public static BoundingBox copyOf(BoundingBox original) {
        BoundingBox box = new BoundingBox();
        box.minCorner.copy(original.minCorner);
        box.maxCorner.copy(original.maxCorner);
        return box;
    }

    /**
     * Returns the displacement of the maximum corner from the minimum corner.
     * @return the displacement of the maximum corner from the minimum corner
     */
    public Vector getSize() {
        return maxCorner.clone().subtract(minCorner);
    }

}
