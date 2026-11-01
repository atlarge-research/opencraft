package science.atlarge.opencraft.opencraft.command;

import static org.junit.Assert.assertEquals;

import org.bukkit.Location;
import org.junit.Test;

public class CommandUtilsTest {

    private static final double DELTA = 0.0001;

    @Test
    public void absoluteIntegerCoordinatesAreCenteredOnTheBlock() {
        Location result = CommandUtils.getLocation(new Location(null, 0, 0, 0), "5", "64", "-5");

        assertEquals(5.5, result.getX(), DELTA);
        assertEquals(64, result.getY(), DELTA);
        assertEquals(-4.5, result.getZ(), DELTA);
    }

    @Test
    public void absoluteDecimalCoordinatesAreNotChanged() {
        Location result = CommandUtils.getLocation(new Location(null, 0, 0, 0), "-5.25", "1", "7.0");

        assertEquals(-5.25, result.getX(), DELTA);
        assertEquals(7.0, result.getZ(), DELTA);
    }

    @Test
    public void relativeOffsetsAreNotCentered() {
        Location result = CommandUtils.getLocation(new Location(null, -13.5, 72, -117.5), "~2", "~", "~-3");

        assertEquals(-11.5, result.getX(), DELTA);
        assertEquals(72, result.getY(), DELTA);
        assertEquals(-120.5, result.getZ(), DELTA);
    }
}
