package science.atlarge.opencraft.opencraft.block.blocktype;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import org.bukkit.block.BlockFace;

public abstract class BlockClimbable extends BlockType {

    @Override
    public boolean canPlaceAt(GlowPlayer player, GlowBlock block, BlockFace against) {
        return against != BlockFace.DOWN && against != BlockFace.UP && isTargetOccluding(block,
            against.getOppositeFace());
    }

    protected boolean isTargetOccluding(GlowBlockState state, BlockFace face) {
        return isTargetOccluding(state.getBlock(), face);
    }

    protected boolean isTargetOccluding(GlowBlock block, BlockFace face) {
        return block.getRelative(face).getType().isOccluding();
    }
}
