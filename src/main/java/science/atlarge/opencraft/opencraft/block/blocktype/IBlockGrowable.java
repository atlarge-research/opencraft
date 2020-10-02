package science.atlarge.opencraft.opencraft.block.blocktype;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;

/**
 * Represents a growable block.
 */
public interface IBlockGrowable {

    /**
     * Called to check if a block can be fertilized.
     *
     * @param block the targeted block to fertilize
     * @return True if the block can be fertilized.
     */
    boolean isFertilizable(GlowBlock block);

    /**
     * Called to check if the block will effectively grow.
     *
     * @param block the targeted block to grow
     * @return True if the block will grow.
     */
    boolean canGrowWithChance(GlowBlock block);

    /**
     * Called to grow a growable block.
     *
     * @param player the player who triggered the growth, this can be null if the growth is natural
     *         or by plugin source
     * @param block the targeted block to grow
     */
    void grow(GlowPlayer player, GlowBlock block);
}
