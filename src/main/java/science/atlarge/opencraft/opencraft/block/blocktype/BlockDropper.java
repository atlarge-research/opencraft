package science.atlarge.opencraft.opencraft.block.blocktype;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.entity.BlockEntity;
import science.atlarge.opencraft.opencraft.block.entity.DropperEntity;
import science.atlarge.opencraft.opencraft.chunk.GlowChunk;
import science.atlarge.opencraft.opencraft.inventory.MaterialMatcher;
import science.atlarge.opencraft.opencraft.inventory.ToolType;

public class BlockDropper extends BlockDispenser {

    @Override
    public BlockEntity createBlockEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return new DropperEntity(chunk.getBlock(cx, cy, cz));
    }

    @Override
    protected MaterialMatcher getNeededMiningTool(GlowBlock block) {
        return ToolType.PICKAXE;
    }
}
