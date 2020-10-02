package science.atlarge.opencraft.opencraft.entity.objects;

import science.atlarge.opencraft.opencraft.entity.GlowHangingEntityTest;
import org.bukkit.block.BlockFace;

public class GlowLeashHitchTest extends GlowHangingEntityTest<GlowLeashHitch> {
    public GlowLeashHitchTest() {
        super(location -> new GlowLeashHitch(location, BlockFace.SOUTH));
    }
}
