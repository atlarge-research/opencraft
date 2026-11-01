package science.atlarge.opencraft.opencraft.entity.objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import science.atlarge.opencraft.opencraft.entity.GlowEntityTest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

public class GlowItemTest extends GlowEntityTest<GlowItem> {
    public GlowItemTest() {
        super(location -> new GlowItem(location, new ItemStack(Material.DIRT)));
    }

    @Test
    public void testItemInTheVoidIsRemoved() {
        when(chunk.isLoaded()).thenReturn(true);
        entity.setRawLocation(new Location(world, 0, -65, 0));
        entity.pulse();
        assertTrue(entity.isDead());
    }

    @Test
    public void testItemAboveTheVoidIsKept() {
        when(chunk.isLoaded()).thenReturn(true);
        entity.setRawLocation(new Location(world, 0, -63, 0));
        entity.pulse();
        assertFalse(entity.isDead());
    }
}
