package science.atlarge.opencraft.opencraft.entity.passive;

import static org.junit.Assert.assertEquals;

import java.util.EnumSet;
import science.atlarge.opencraft.opencraft.entity.GlowAnimalTest;
import org.bukkit.Material;
import org.junit.Test;

public class GlowRabbitTest extends GlowAnimalTest<GlowRabbit> {
    public GlowRabbitTest() {
        super(GlowRabbit::new);
    }

    @Test
    @Override
    public void testGetBreedingFoods() {
        assertEquals(EnumSet.of(Material.YELLOW_FLOWER, Material.GOLDEN_CARROT, Material.CARROT_ITEM),
                entity.getBreedingFoods());
    }
}
