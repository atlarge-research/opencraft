package science.atlarge.opencraft.opencraft.block.blocktype;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.entity.BlockEntity;
import science.atlarge.opencraft.opencraft.block.entity.EnchantingTableEntity;
import science.atlarge.opencraft.opencraft.chunk.GlowChunk;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.inventory.MaterialMatcher;
import science.atlarge.opencraft.opencraft.inventory.ToolType;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class BlockEnchantmentTable extends BlockNeedsTool {

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face,
                                 Vector clickedLoc) {
        return player.openEnchanting(block.getLocation(), false) != null;
    }

    @Override
    public BlockEntity createBlockEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return new EnchantingTableEntity(chunk.getBlock(cx, cy, cz));
    }

    @Override
    protected MaterialMatcher getNeededMiningTool(GlowBlock block) {
        return ToolType.PICKAXE;
    }
}
