package net.glowstone.entity.physics;

import net.glowstone.util.Vectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * A BoundingBox which changes position over time as an entity moves.
 */
public class EntityBoundingBox extends BoundingBox {

    private final double width;
    private final double vertSize;
    private final double depth;

    public EntityBoundingBox(double horizSize, double vertSize) {
        this(horizSize, vertSize, horizSize);
    }

    /**
     * Creates an instance with the given size.
     *
     * @param width the size on the X axis
     * @param vertSize the size on the Y axis
     * @param depth the size on the Z axis
     */
    public EntityBoundingBox(double width, double vertSize, double depth) {
        this.width = width;
        this.vertSize = vertSize;
        this.depth = depth;
    }


    /**
     * Calculates the broadPhase of an entity with a given velocity.
     * The broadPhase is used as any collision check box by giving the entire area the velocity includes
     *
     * @param velocity the velocity of the entity
     * @return The broadphase of the entity with velocity v
     */
    public BoundingBox getBroadPhase(Vector velocity) {

        Vector min = minCorner;
        Vector max = maxCorner;

        BoundingBox broadPhase = new BoundingBox();

        broadPhase.minCorner.setX(min.getX() + Math.min(0.0, velocity.getX()));
        broadPhase.minCorner.setY(min.getY() + Math.min(0.0, velocity.getY()));
        broadPhase.minCorner.setZ(min.getZ() + Math.min(0.0, velocity.getZ()));

        broadPhase.maxCorner.setX(max.getX() + Math.max(0.0, velocity.getX()));
        broadPhase.maxCorner.setY(max.getY() + Math.max(0.0, velocity.getY()));
        broadPhase.maxCorner.setZ(max.getZ() + Math.max(0.0, velocity.getZ()));

        return broadPhase;
    }

    /**
     * Returns the inverse exit position vector.
     *
     * @param velocity The velocity of the entity
     * @param staticBox The bounding box to check collision with
     * @return The inverse exit position vector
     */
    private Vector getInverseExit(Vector velocity, BoundingBox staticBox) {

        Vector inverseExit = new Vector();

        if (velocity.getX() > 0.0f) {
            inverseExit.setX(staticBox.maxCorner.getX() - this.minCorner.getX());
        } else {
            inverseExit.setX(staticBox.minCorner.getX() - this.maxCorner.getX());
        }

        if (velocity.getY() > 0.0f) {
            inverseExit.setY(staticBox.maxCorner.getY() - this.minCorner.getY());
        } else {
            inverseExit.setY(staticBox.minCorner.getY() - this.maxCorner.getY());
        }

        if (velocity.getZ() > 0.0f) {
            inverseExit.setZ(staticBox.maxCorner.getZ() - this.minCorner.getZ());
        } else {
            inverseExit.setZ(staticBox.minCorner.getZ() - this.maxCorner.getZ());
        }

        return inverseExit;
    }

    /**
     * Returns the inverse entry position vector.
     *
     * @param velocity The velocity of the entity
     * @param staticBox The bounding box to check collision with
     * @return The inverse entry position vector
     */
    private Vector getInverseEntry(Vector velocity, BoundingBox staticBox) {

        Vector inverseEntry = new Vector();

        if (velocity.getX() > 0.0f) {
            inverseEntry.setX(staticBox.minCorner.getX() - maxCorner.getX());
        } else {
            inverseEntry.setX(staticBox.maxCorner.getX() - minCorner.getX());
        }

        if (velocity.getY() > 0.0f) {
            inverseEntry.setY(staticBox.minCorner.getY() - maxCorner.getY());
        } else {
            inverseEntry.setY(staticBox.maxCorner.getY() - minCorner.getY());
        }

        if (velocity.getZ() > 0.0f) {
            inverseEntry.setZ(staticBox.minCorner.getZ() - maxCorner.getZ());
        } else {
            inverseEntry.setZ(staticBox.maxCorner.getZ() - minCorner.getZ());
        }

        return inverseEntry;
    }

    /**
     * Returns the surface normal of the surface that gets collided with first.
     *
     * @param entry The entry vector of the collision
     * @param inverseEntry The inverse entry vector
     * @return The normal with which collision occurs
     */
    private Vector getClosestNormalSurface(Vector entry, Vector inverseEntry) {

        Vector normal = new Vector(0.0, 0.0, 0.0);

        if (entry.getZ() > entry.getX()) {
            if (entry.getZ() > entry.getY()) {
                if (inverseEntry.getZ() < 0.0d) {
                    normal.setZ(1.0d);
                } else {
                    normal.setZ(-1.0d);
                }
            } else {
                if (inverseEntry.getY() < 0.0d) {
                    normal.setY(1.0d);
                } else {
                    normal.setY(-1.0d);
                }
            }
        } else {
            if (entry.getX() > entry.getY()) {
                if (inverseEntry.getX() < 0.0d) {
                    normal.setX(1.0d);
                } else {
                    normal.setX(-1.0d);
                }
            } else {
                if (inverseEntry.getY() < 0.0d) {
                    normal.setY(1.0d);
                } else {
                    normal.setY(-1.0d);
                }
            }
        }

        return normal;
    }


    /**
     * This function implements swept AABB collision detection as specified in the link below
     * https://www.gamedev.net/tutorials/programming/general-and-gameplay-programming/swept-aabb-collision-detection-and-response-r3084/
     *
     * @param velocity The displacement that will be applied to the entityboundingbox
     * @param staticBox The static box which will be checked for collision
     * @return A pair containing a double between 0.0 and 1.0 that indicates the amount
     *      of displacement that can be done without collision
     *      And a vector containing the normal in the place of collision
     */
    public Pair<Double, Vector> sweptAxisAlignedBoundingBox(Vector velocity, BoundingBox staticBox) {

        Vector inverseEntry = getInverseEntry(velocity, staticBox);
        Vector inverseExit = getInverseExit(velocity, staticBox);

        Vector entry = inverseEntry.divide(velocity);
        Vector exit = inverseExit.divide(velocity);

        Vectors.flipInfinity(entry);
        Vectors.flipInfinity(exit);

        double entryTime = Math.max(entry.getX(), Math.max(entry.getY(), entry.getZ()));
        double exitTime = Math.min(exit.getX(), Math.min(exit.getY(), exit.getZ()));

        boolean isEntryOnNegativeVelocity = entry.getX() < 0.0d && entry.getY() < 0.0d && entry.getZ() < 0.0d;
        boolean isEntryAfterVelocity = entry.getX() > 1.0d || entry.getY() > 1.0d || entry.getZ() > 1.0d;

        if (entryTime > exitTime || isEntryOnNegativeVelocity || isEntryAfterVelocity) {
            return new ImmutablePair<>(1.0d, new Vector(0.0d, 0.0d, 0.0d));
        } else {
            Vector normal = getClosestNormalSurface(entry, inverseEntry);
            return new ImmutablePair<>(entryTime, normal);
        }
    }

    @Override
    public Vector getSize() {
        return new Vector(width, vertSize, depth);
    }

    /**
     * Moves this box so that its center is the given point.
     *
     * @param x the center X coordinate
     * @param y the center Y coordinate
     * @param z the center Z coordinate
     */
    public void setCenter(double x, double y, double z) {
        minCorner.setX(x - width / 2);
        minCorner.setY(y);
        minCorner.setZ(z - depth / 2);
        maxCorner.setX(x + width / 2);
        maxCorner.setY(y + vertSize);
        maxCorner.setZ(z + depth / 2);
    }

    /**
     * Create a copy of the current bounding box and center it at the given location.
     *
     * @param center the location of the new bounding box.
     * @return a copy of the current bounding box.
     */
    public EntityBoundingBox createCopyAt(Location center) {
        EntityBoundingBox box = new EntityBoundingBox(width, vertSize, depth);
        box.setCenter(center.getX(), center.getY(), center.getZ());
        return box;
    }
}
