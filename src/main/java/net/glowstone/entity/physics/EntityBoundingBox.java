package net.glowstone.entity.physics;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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

        broadPhase.minCorner.setX(v.getX() > 0 ? min.getX() : min.getX() + v.getX());
        broadPhase.minCorner.setY(v.getY() > 0 ? min.getY() : min.getY() + v.getY());
        broadPhase.minCorner.setZ(v.getZ() > 0 ? min.getZ() : min.getZ() + v.getZ());

        broadPhase.maxCorner.setX(v.getX() > 0 ? max.getX() + v.getX() : max.getX() - v.getX());
        broadPhase.maxCorner.setY(v.getY() > 0 ? max.getY() + v.getY() : max.getY() - v.getY());
        broadPhase.maxCorner.setZ(v.getZ() > 0 ? max.getZ() + v.getZ() : max.getZ() - v.getZ());


        return broadPhase;
    }


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
            return new ImmutablePair<>(1.0d, normal);
        } else {
            // calculate normal of collided surface
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

}
