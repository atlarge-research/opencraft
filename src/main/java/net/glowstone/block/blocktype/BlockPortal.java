package net.glowstone.block.blocktype;

import net.glowstone.GlowWorld;
import net.glowstone.block.GlowBlock;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class BlockPortal extends BlockType {

    /**
     * This method checks if the direction corresponds to the data of a portal block. Data value 1 corresponds to
     * east and west, while data value 2 corresponds to north and south. A portal above or below should never be a
     * problem.
     *
     * @param data The data value of the block.
     * @param direction the direction to check for adjacent block.
     * @return If the direction corresponds to the data.
     */
    private boolean isPortalDirection(byte data, BlockFace direction) {
        if (direction == BlockFace.DOWN || direction == BlockFace.UP) {
            return true;
        }

        if (data == 1 && (direction == BlockFace.EAST || direction == BlockFace.WEST)) {
            return true;
        } else if (data == 2 && (direction == BlockFace.NORTH || direction == BlockFace.SOUTH)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onNearBlockChanged(
            GlowBlock block,
            BlockFace face,
            GlowBlock changedBlock,
            Material oldType,
            byte oldData,
            Material newType,
            byte newData
    ) {
        int portalCount = 0;
        int obsidianCount = 0;
        byte blockData = block.getData();

        for (BlockFace side : ADJACENT) {

            GlowBlock adjacent = block.getRelative(side);
            Material type = adjacent.getType();

            if (type == Material.PORTAL && isPortalDirection(blockData, side)) {
                portalCount++;
            }

            if (type == Material.OBSIDIAN) {
                obsidianCount++;
            }
        }

        if (portalCount + obsidianCount < 4 || portalCount < 2) {
            block.breakNaturally();
        }
    }
}
