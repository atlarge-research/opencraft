package net.glowstone.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.glowstone.chunk.AreaOfInterest;
import org.bukkit.Location;
import org.junit.jupiter.api.Test;

/**
 * Test class for the AreaOfInterest
 */
public class AreaOfInterestTest {

    /**
     * Test that equals works with a null location
     */
    @Test
    void equalsTest() {
        Location locationOne = new Location(null, 1, 2, 3);
        Location locationTwo = new Location(null, 1, 2, 3);
        AreaOfInterest areaOfInterestOne = new AreaOfInterest(locationOne, 3);
        AreaOfInterest areaOfInterestTwo = new AreaOfInterest(locationTwo, 3);
        assertEquals(areaOfInterestOne, areaOfInterestTwo);
    }
}
