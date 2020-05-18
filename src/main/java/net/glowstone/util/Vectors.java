package net.glowstone.util;

import org.bukkit.util.Vector;

public class Vectors {

    public static Vector floor(Vector vector) {
        return new Vector(
                Math.floor(vector.getX()),
                Math.floor(vector.getY()),
                Math.floor(vector.getZ())
        );
    }

    public static Vector ceil(Vector vector) {
        return new Vector(
                Math.ceil(vector.getX()),
                Math.ceil(vector.getY()),
                Math.ceil(vector.getZ())
        );
    }

    public static Vector project(Vector vector, Vector normal) {
        double dot = vector.dot(normal);
        return normal.clone().multiply(dot);
    }

    public static boolean equals(Vector vector, Vector otherVector, double epsilon) {
        double dx = Math.abs(vector.getX() - otherVector.getX());

        if (dx >= epsilon) {
            return false;
        }

        double dy = Math.abs(vector.getY() - otherVector.getY());

        if (dy >= epsilon) {
            return false;
        }

        double dz = Math.abs(vector.getZ() - otherVector.getZ());

        if (dz >= epsilon) {
            return false;
        }

        return true;
    }

    public static boolean equals(Vector vector, Vector otherVector) {
        return equals(vector,otherVector, Double.MIN_VALUE);
    }
}
