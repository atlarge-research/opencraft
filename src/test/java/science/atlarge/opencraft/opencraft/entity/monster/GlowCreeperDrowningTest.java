package science.atlarge.opencraft.opencraft.entity.monster;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.bukkit.Material;
import org.junit.Test;
import science.atlarge.opencraft.opencraft.entity.GlowEntityTest;

/**
 * Tests the drowning rules of living entities, which are shared by all of them.
 */
public class GlowCreeperDrowningTest extends GlowEntityTest<GlowCreeper> {

    public GlowCreeperDrowningTest() {
        super(GlowCreeper::new);
    }

    @Test
    public void testNoDamageWhileAirLasts() {
        when(block.getType()).thenReturn(Material.STATIONARY_WATER);
        double health = entity.getHealth();

        for (int tick = 0; tick < entity.getMaximumAir() + 19; tick++) {
            entity.pulse();
        }

        assertEquals(health, entity.getHealth(), 0);
    }

    @Test
    public void testDrowningDealsTwoHealthWhenTheAirIsGone() {
        when(block.getType()).thenReturn(Material.STATIONARY_WATER);
        double health = entity.getHealth();

        // The air lasts 300 ticks, and the damage follows 20 ticks after it runs out.
        for (int tick = 0; tick < entity.getMaximumAir() + 20; tick++) {
            entity.pulse();
        }

        assertEquals(health - 2, entity.getHealth(), 0);
    }
}
