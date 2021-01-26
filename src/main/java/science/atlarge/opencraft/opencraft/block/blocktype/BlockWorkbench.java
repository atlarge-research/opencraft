package science.atlarge.opencraft.opencraft.block.blocktype;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class BlockWorkbench extends BlockType {

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face,
        Vector clickedLoc) {
        return player.openWorkbench(block.getLocation(), false) != null;
    }
}
