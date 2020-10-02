package science.atlarge.opencraft.opencraft.entity.passive;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import science.atlarge.opencraft.opencraft.entity.GlowEntityTest;
import org.junit.Test;

public class GlowBatTest extends GlowEntityTest<GlowBat> {
    public GlowBatTest() {
        super(GlowBat::new);
    }

    @Test
    public void testSleep() {
        entity.setAwake(false);
        assertFalse(entity.isAwake());
    }

    @Test
    public void testWakeUp() {
        entity.setAwake(true);
        assertTrue(entity.isAwake());
    }
}
