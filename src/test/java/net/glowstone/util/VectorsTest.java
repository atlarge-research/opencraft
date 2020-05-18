package net.glowstone.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.util.Vector;
import org.junit.Test;

public class VectorsTest {

    private double EPSILON = 0.0000000000001d;

    @Test
    public void floorTest(){
        Vector vector = new Vector(0.1, 1.0, -0.9);
        Vector floored = Vectors.floor(vector);
        assertEquals(floored.getX(), 0.0, EPSILON);
        assertEquals(floored.getY(), 1.0, EPSILON);
        assertEquals(floored.getZ(), -1.0, EPSILON);
    }

    @Test
    public void ceilTest(){
        Vector vector = new Vector(0.1, 1.0, -0.9);
        Vector ceiled = Vectors.ceil(vector);
        assertEquals(ceiled.getX(), 1.0, EPSILON);
        assertEquals(ceiled.getY(), 1.0, EPSILON);
        assertEquals(ceiled.getZ(), 0.0, EPSILON);
    }

    @Test
    public void projectTest(){
        Vector vector = new Vector(0.1, 1.0, -0.9);
        Vector normal = new Vector(1.0, 0.0, 0.0);
        Vector projected = Vectors.project(vector, normal);

        assertEquals(projected.getX(), 0.1, EPSILON);
        assertEquals(projected.getY(), 0.0, EPSILON);
        assertEquals(projected.getZ(), 0.0, EPSILON);
    }

    @Test
    public void projectTest1(){
        Vector vector = new Vector(0.1, 1.0, -0.9);
        Vector normal = new Vector(0.0, 1.0, 0.0);
        Vector projected = Vectors.project(vector, normal);

        assertEquals(projected.getX(), 0.0, EPSILON);
        assertEquals(projected.getY(), 1.0, EPSILON);
        assertEquals(projected.getZ(), 0.0, EPSILON);
    }

    @Test
    public void projectTest2(){
        Vector vector = new Vector(0.1, 1.0, -0.9);
        Vector normal = new Vector(0.0, 0.0, 1.0);
        Vector projected = Vectors.project(vector, normal);

        assertEquals(projected.getX(), 0.0, EPSILON);
        assertEquals(projected.getY(), 0.0, EPSILON);
        assertEquals(projected.getZ(), -0.9, EPSILON);
    }
}
