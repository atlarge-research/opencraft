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
}
