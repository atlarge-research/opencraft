package net.glowstone.entity.physics;

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

        broadPhase.maxCorner.setX(max.getX() + Math.abs(velocity.getX()));
        broadPhase.maxCorner.setY(max.getY() + Math.abs(velocity.getY()));
        broadPhase.maxCorner.setZ(max.getZ() + Math.abs(velocity.getZ()));

        return broadPhase;
    }


    /**
     * This function implements swept AABB collision detection as specified in the link below
     * https://www.gamedev.net/tutorials/programming/general-and-gameplay-programming/swept-aabb-collision-detection-and-response-r3084/
     *
     * @param vel The displacement that will be applied to the entityboundingbox
     * @param staticBox The static box which will be checked for collision
     * @return A pair containing a double between 0.0 and 1.0 that indicates the amount
     *      of displacement that can be done without collision
     *      And a vector containing the normal in the place of collision
     */
    public Pair<Double, Vector> sweptAxisAlignedBoundingBox(Vector vel, BoundingBox staticBox) {

        double invEntryX;
        double invEntryY;
        double invEntryZ;
        double invExitX;
        double invExitY;
        double invExitZ;

        Vector normal = new Vector();

        // find the distance between the objects on the near and far sides for both x,y and z
        if (vel.getX() > 0.0f) {
            invEntryX = staticBox.minCorner.getX() - this.maxCorner.getX();
            invExitX = staticBox.maxCorner.getX() - this.minCorner.getX();
        } else {
            invEntryX = staticBox.maxCorner.getX() - this.minCorner.getX();
            invExitX = staticBox.minCorner.getX() - this.maxCorner.getX();
        }

        if (vel.getY() > 0.0f) {
            invEntryY = staticBox.minCorner.getY() - this.maxCorner.getY();
            invExitY = staticBox.maxCorner.getY() - this.minCorner.getY();
        } else {
            invEntryY = staticBox.maxCorner.getY() - this.minCorner.getY();
            invExitY = staticBox.minCorner.getY() - this.maxCorner.getY();
        }

        if (vel.getZ() > 0.0f) {
            invEntryZ = staticBox.minCorner.getZ() - this.maxCorner.getZ();
            invExitZ = staticBox.maxCorner.getZ() - this.minCorner.getZ();
        } else {
            invEntryZ = staticBox.maxCorner.getZ() - this.minCorner.getZ();
            invExitZ = staticBox.minCorner.getZ() - this.maxCorner.getZ();
        }

        double entryX;
        double entryY;
        double entryZ;
        double exitX;
        double exitY;
        double exitZ;

        // If the velocity is 0 then there is no exit or entry on that axis
        if (vel.getX() == 0.0f) {
            entryX = Double.NEGATIVE_INFINITY;
            exitX = Double.POSITIVE_INFINITY;
        } else {
            entryX = invEntryX / vel.getX();
            exitX = invExitX / vel.getX();
        }

        if (vel.getY() == 0.0f) {
            entryY = Double.NEGATIVE_INFINITY;
            exitY = Double.POSITIVE_INFINITY;
        } else {
            entryY = invEntryY / vel.getY();
            exitY = invExitY / vel.getY();
        }

        if (vel.getZ() == 0.0f) {
            entryZ = Double.NEGATIVE_INFINITY;
            exitZ = Double.POSITIVE_INFINITY;
        } else {
            entryZ = invEntryZ / vel.getZ();
            exitZ = invExitZ / vel.getZ();
        }

        double entryTime = Math.max(entryX, Math.max(entryY, entryZ));
        double exitTime = Math.min(exitX, Math.min(exitY, exitZ));

        normal.setX(0.0d);
        normal.setY(0.0d);
        normal.setZ(0.0d);

        if (entryTime > exitTime || entryX < 0.0d && entryY < 0.0d && entryZ < 0.0d
                || entryX > 1.0d || entryY > 1.0d || entryZ > 1.0d) {
            // If there are no collision return 1.0d indicating that the full velocity vector can be applied
            return new ImmutablePair<>(1.0d, normal);
        } else {
            // Find the shortest entry distance, take the normal from that collision
            if (entryZ > entryX) {
                if (entryZ > entryY) {
                    if (invEntryZ < 0.0d) {
                        normal.setZ(1.0d);
                    } else {
                        normal.setZ(-1.0d);
                    }
                } else {
                    if (invEntryY < 0.0d) {
                        normal.setY(1.0d);
                    } else {
                        normal.setY(-1.0d);
                    }
                }
            } else {
                if (entryX > entryY) {
                    if (invEntryX < 0.0d) {
                        normal.setX(1.0d);
                    } else {
                        normal.setX(-1.0d);
                    }
                } else {
                    if (invEntryY < 0.0d) {
                        normal.setY(1.0d);
                    } else {
                        normal.setY(-1.0d);
                    }
                }
            }
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
