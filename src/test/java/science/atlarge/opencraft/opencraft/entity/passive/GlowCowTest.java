package science.atlarge.opencraft.opencraft.entity.passive;

import static org.junit.Assert.assertEquals;

import java.util.EnumSet;
import science.atlarge.opencraft.opencraft.entity.GlowAnimalTest;
import org.bukkit.Material;
import org.junit.Test;

public class GlowCowTest extends GlowAnimalTest<GlowCow> {

    public GlowCowTest() {
        super(GlowCow::new);
    }

    @Test
    @Override
    public void testGetBreedingFoods() {
        assertEquals(EnumSet.of(Material.WHEAT), entity.getBreedingFoods());
    }
}
