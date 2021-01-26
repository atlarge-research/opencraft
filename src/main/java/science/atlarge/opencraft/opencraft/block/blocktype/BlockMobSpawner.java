package science.atlarge.opencraft.opencraft.block.blocktype;

import science.atlarge.opencraft.opencraft.block.entity.BlockEntity;
import science.atlarge.opencraft.opencraft.block.entity.MobSpawnerEntity;
import science.atlarge.opencraft.opencraft.chunk.GlowChunk;

public class BlockMobSpawner extends BlockDropless {

    @Override
    public BlockEntity createBlockEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return new MobSpawnerEntity(chunk.getBlock(cx, cy, cz));
    }
}
