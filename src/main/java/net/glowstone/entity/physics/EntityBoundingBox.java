package net.glowstone.entity.physics;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collection;

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
     * @param width    the size on the X axis
     * @param vertSize the size on the Y axis
     * @param depth    the size on the Z axis
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
     * @param v the velocity of the entity
     * @return The broadphase of the entity with velocity v
     */
    public BoundingBox getBroadPhase(Vector v) {

        Vector min = minCorner;
        Vector max = maxCorner;

        BoundingBox broadPhase = new BoundingBox();
        
        broadPhase.minCorner.setX(min.getX() + Math.min(0.0, v.getX()));
        broadPhase.minCorner.setY(min.getY() + Math.min(0.0, v.getY()));
        broadPhase.minCorner.setZ(min.getZ() + Math.min(0.0, v.getZ()));

        broadPhase.maxCorner.setX(max.getX() + Math.abs(v.getX()));
        broadPhase.maxCorner.setY(max.getY() + Math.abs(v.getY()));
        broadPhase.maxCorner.setZ(max.getZ() + Math.abs(v.getZ()));

        return broadPhase;
    }


    /**
     * This function implements swept AABB collision detection as specified in the link below
     * https://www.gamedev.net/tutorials/programming/general-and-gameplay-programming/swept-aabb-collision-detection-and-response-r3084/
     *
     * @param vel The displacement that will be applied to the entityboundingbox
     * @param staticBox The static box which will be checked for collision
     * @return A pair containing a double between 0.0 and 1.0 that indicates the amount
     *         of displacement that can be done without collision
     *         And a vector containing the normal in the place of collision
     */
    public Pair<Double, Vector> sweptAABB(Vector vel, BoundingBox staticBox) {

        double xInvEntry, yInvEntry, zInvEntry;
        double xInvExit, yInvExit, zInvExit;
        Vector normal = new Vector();

        // find the distance between the objects on the near and far sides for both x,y and z
        if (vel.getX() > 0.0f) {
            xInvEntry = staticBox.minCorner.getX() - this.maxCorner.getX();
            xInvExit = staticBox.maxCorner.getX() - this.minCorner.getX();
        } else {
            xInvEntry = staticBox.maxCorner.getX() - this.minCorner.getX();
            xInvExit = staticBox.minCorner.getX() - this.maxCorner.getX();
        }

        if (vel.getY() > 0.0f) {
            yInvEntry = staticBox.minCorner.getY() - this.maxCorner.getY();
            yInvExit = staticBox.maxCorner.getY() - this.minCorner.getY();
        } else {
            yInvEntry = staticBox.maxCorner.getY() - this.minCorner.getY();
            yInvExit = staticBox.minCorner.getY() - this.maxCorner.getY();
        }

        if (vel.getZ() > 0.0f) {
            zInvEntry = staticBox.minCorner.getZ() - this.maxCorner.getZ();
            zInvExit = staticBox.maxCorner.getZ() - this.minCorner.getZ();
        } else {
            zInvEntry = staticBox.maxCorner.getZ() - this.minCorner.getZ();
            zInvExit = staticBox.minCorner.getZ() - this.maxCorner.getZ();
        }

        double xEntry, yEntry, zEntry;
        double xExit, yExit, zExit;

        // If the velocity is 0 then there is no exit or entry on that axis
        if (vel.getX() == 0.0f) {
            xEntry = Double.NEGATIVE_INFINITY;
            xExit = Double.POSITIVE_INFINITY;
        } else {
            xEntry = xInvEntry / vel.getX();
            xExit = xInvExit / vel.getX();
        }

        if (vel.getY() == 0.0f) {
            yEntry = Double.NEGATIVE_INFINITY;
            yExit = Double.POSITIVE_INFINITY;
        } else {
            yEntry = yInvEntry / vel.getY();
            yExit = yInvExit / vel.getY();
        }

        if (vel.getZ() == 0.0f) {
            zEntry = Double.NEGATIVE_INFINITY;
            zExit = Double.POSITIVE_INFINITY;
        } else {
            zEntry = zInvEntry / vel.getZ();
            zExit = zInvExit / vel.getZ();
        }

        double entryTime = Math.max(xEntry, Math.max(yEntry, zEntry));
        double exitTime = Math.min(xExit, Math.min(yExit, zExit));

        normal.setX(0.0d);
        normal.setY(0.0d);
        normal.setZ(0.0d);

        if (entryTime > exitTime || xEntry < 0.0d && yEntry < 0.0d  && zEntry < 0.0d|| xEntry > 1.0d || yEntry > 1.0d || zEntry > 1.0d) {
            // If there are no collision return 1.0d indicating that the full velocity vector can be applied
            return new ImmutablePair<>(1.0d, normal);
        } else {
            // Find the shortest entry distance, take the normal from that collision
            if (zEntry > xEntry) {
                if (zEntry > yEntry) {
                    if (zInvEntry < 0.0d) {
                        normal.setZ(1.0d);
                    } else {
                        normal.setZ(-1.0d);
                    }
                } else {
                    if (yInvEntry < 0.0d) {
                        normal.setY(1.0d);
                    } else {
                        normal.setY(-1.0d);
                    }
                }
            } else {
                if (xEntry > yEntry) {
                    if (xInvEntry < 0.0d) {
                        normal.setX(1.0d);
                    } else {
                        normal.setX(-1.0d);
                    }
                } else {
                    if (yInvEntry < 0.0d) {
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
