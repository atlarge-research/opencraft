package science.atlarge.opencraft.opencraft.entity.objects;

import science.atlarge.opencraft.opencraft.entity.GlowHangingEntityTest;
import org.bukkit.block.BlockFace;

public class GlowItemFrameTest extends GlowHangingEntityTest<GlowItemFrame> {
    public GlowItemFrameTest() {
        super(location -> new GlowItemFrame(null, location, BlockFace.SOUTH));
    }
}
