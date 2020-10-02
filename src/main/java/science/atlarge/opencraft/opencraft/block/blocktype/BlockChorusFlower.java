package science.atlarge.opencraft.opencraft.block.blocktype;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class BlockChorusFlower extends BlockType {

    @Override
    public boolean canPlaceAt(GlowPlayer player, GlowBlock block, BlockFace against) {
        GlowBlock under = block.getRelative(BlockFace.DOWN);
        if (under.getType() == Material.ENDER_STONE || under.getType() == Material.CHORUS_PLANT) {
            return true;
        } else if (under.getType() == Material.AIR) {
            boolean hasSupport = false;
            for (BlockFace side : SIDES) {
                GlowBlock relative = block.getRelative(side);
                if (relative.getType() == Material.CHORUS_PLANT) {
                    if (hasSupport) { //only one chorus plant allowed on the side
                        return false;
                    } else {
                        hasSupport = true;
                    }
                }
            }
            if (hasSupport) {
                return true;
            }
        }
        return false;
    }
}
