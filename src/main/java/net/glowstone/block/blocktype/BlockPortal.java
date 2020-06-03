package net.glowstone.block.blocktype;

import java.util.Collection;
import java.util.Collections;
import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class BlockPortal extends BlockType {


    /**
     * This method checks whether the direction corresponds to the data of a portal block. Data value 1 corresponds to
     * east and west, while data value 2 corresponds to north and south. There should always be a block above and
     * below a portal block.
     *
     * @param data The data value of the portal block.
     * @param direction the direction to check for adjacent block.
     * @return If the direction corresponds to the data.
     */
    private boolean isPortalDirection(byte data, BlockFace direction) {

        if (direction == BlockFace.DOWN || direction == BlockFace.UP) {
            return true;
        }

        if (data == 1 && (direction == BlockFace.EAST || direction == BlockFace.WEST)) {
            return true;
        } else if (data == 2 && (direction == BlockFace.NORTH || direction == BlockFace.SOUTH)) {
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
        int count = 0;
        byte blockData = block.getData();

        for (BlockFace side : ADJACENT) {

            GlowBlock adjacent = block.getRelative(side);
            Material type = adjacent.getType();

            if (!isPortalDirection(blockData, side)) {
                continue;
            }

            if (type == Material.PORTAL || type == Material.OBSIDIAN) {
                count++;
            }
        }

        if (count < 4) {
            block.breakNaturally();
        }
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
        return Collections.emptyList();
    }
}
