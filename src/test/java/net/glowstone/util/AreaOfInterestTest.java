package net.glowstone.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bukkit.Location;
import org.junit.jupiter.api.Test;

/**
 * Test class for the AreaOfInterest
 */
public class AreaOfInterestTest {

    /**
     * Test that a null location is correctly retrieved.
     */
    @Test
    void getNullLocationTest() {
        AreaOfInterest areaOfInterest = new AreaOfInterest(null, 0);

        assertTrue(areaOfInterest.getLocation() == null);
    }

    /**
     * Test that the location is cloned when getter is called.
     */
    @Test
    void getLocationTest() {
        Location location = new Location(null, 1, 1, 1);
        AreaOfInterest areaOfInterest = new AreaOfInterest(location, 0);

        Location cloneLocation = areaOfInterest.getLocation();
        cloneLocation.setX(3);

        assertFalse(location.equals(cloneLocation));
    }

    /**
     * Test that a null view distance is returned correctly.
     */
    @Test
    void getNullViewDistanceTest() {
        AreaOfInterest areaOfInterest = new AreaOfInterest(null, null);

        assertTrue(areaOfInterest.getViewDistance() == null);
    }

    /**
     * Test the view distance is returned when the getter is called.
     */
    @Test
    void getViewDistanceTest() {
        AreaOfInterest areaOfInterest = new AreaOfInterest(null, 4);

        assertTrue(areaOfInterest.getViewDistance() == 4);
    }

    /**
     * Test that equals works with a null location
     */
    @Test
    void equalsNullTest() {
        AreaOfInterest areaOfInterestOne = new AreaOfInterest(null, 0);
        AreaOfInterest areaOfInterestTwo = new AreaOfInterest(null, 0);
        assertTrue(areaOfInterestOne.equals(areaOfInterestTwo));
    }

    /**
     * Test that equals works with a null location
     */
    @Test
    void equalsTest() {
        Location locationOne = new Location(null, 1, 2, 3);
        Location locationTwo = new Location(null, 1, 2, 3);
        AreaOfInterest areaOfInterestOne = new AreaOfInterest(locationOne, 3);
        AreaOfInterest areaOfInterestTwo = new AreaOfInterest(locationTwo, 3);
        assertTrue(areaOfInterestOne.equals(areaOfInterestTwo));
    }

    /**
     * Test that equals works with a null location
     */
    @Test
    void equalsFalseTest() {
        Location locationOne = new Location(null, 1, 2, 2);
        Location locationTwo = new Location(null, 1, 2, 3);
        AreaOfInterest areaOfInterestOne = new AreaOfInterest(locationOne, 3);
        AreaOfInterest areaOfInterestTwo = new AreaOfInterest(locationTwo, 3);
        assertFalse(areaOfInterestOne.equals(areaOfInterestTwo));
    }
}
