package science.atlarge.opencraft.opencraft.entity.objects;

import science.atlarge.opencraft.opencraft.entity.GlowEntityTest;
import org.bukkit.Material;

public class GlowFallingBlockTest extends GlowEntityTest<GlowFallingBlock> {
    public GlowFallingBlockTest() {
        super(location -> new GlowFallingBlock(location, Material.GRAVEL, (byte) 0));
    }
}
