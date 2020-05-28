package net.glowstone.block.itemtype;

import java.util.Arrays;
import java.util.List;
import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockTnt;
import net.glowstone.entity.GlowPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemFlintAndSteel extends ItemTool {

    private static final int PORTAL_SIZE = 20;

    public ItemFlintAndSteel() {
        this.setPlaceAs(Material.FIRE);
    }

    @Override
    public boolean onToolRightClick(GlowPlayer player, GlowBlock target, BlockFace face,
        ItemStack holding, Vector clickedLoc, EquipmentSlot hand) {
        if (target.getType() == Material.OBSIDIAN) {
            fireNetherPortal(target, face);
            return true;
        }
        if (target.getType() == Material.TNT) {
            fireTnt(target, player);
            return true;
        }
        if (target.isFlammable() || target.getType().isOccluding()) {
            setBlockOnFire(player, target, face, holding, clickedLoc, hand);
            return true;
        }
        return false;
    }

    private boolean isValidPortal(GlowBlock block) {

        Pair<GlowBlock, BlockFace> portalPos = getPortalPosition(block);
        if (portalPos == null) {
            return false;
        }

        GlowBlock cornerBlock = portalPos.getLeft();
        BlockFace direction = portalPos.getRight();

        int height = getPortalLengthDirection(cornerBlock, BlockFace.UP);
        int width = getPortalLengthDirection(cornerBlock, direction);
        System.out.println("height: " + height + " width: " + width);
        if (height < 3 || width < 2) {
            return false;
        }

        GlowBlock blockFloor = cornerBlock.getRelative(BlockFace.DOWN);
        for (int i = 0; i < width; i++){
            if (blockFloor.getType() != Material.OBSIDIAN) {
                return false;
            }
            blockFloor = blockFloor.getRelative(direction);
        }

        GlowBlock sideBlock = cornerBlock.getRelative(direction.getOppositeFace());
        GlowBlock oppositeSideBlock = cornerBlock.getRelative(direction, width);
        for (int i = 0; i < height; i++){
            if (sideBlock.getType() != Material.OBSIDIAN) {
                return false;
            } else if (oppositeSideBlock.getType() != Material.OBSIDIAN) {
                return false;
            }
            sideBlock = sideBlock.getRelative(BlockFace.UP);
            oppositeSideBlock = oppositeSideBlock.getRelative(BlockFace.UP);
        }

        GlowBlock blockCeiling = cornerBlock.getRelative(BlockFace.UP, height);
        for (int i = 0; i < width; i++){
            if (blockCeiling.getType() != Material.OBSIDIAN) {
                return false;
            }
            blockCeiling = blockCeiling.getRelative(direction);
        }

        return fillPortal(height, width, cornerBlock, direction);
    }

    private boolean fillPortal(int height, int width, GlowBlock cornerBlock, BlockFace direction) {
        GlowBlock current = cornerBlock;
        boolean success = true;
        for (int i = 0; i < height; i++) {
            current = cornerBlock.getRelative(BlockFace.UP, i);
            for (int j = 0; j < width; j++) {
                if (current.getType() != Material.AIR) {
                    success = false;
                }
                current = current.getRelative(direction);
            }
        }

        byte data;
        if (direction == BlockFace.EAST || direction == BlockFace.WEST) {
            data = 1;
        } else {
            data = 2;
        }

        if(success) {
            for (int i = 0; i < height; i++) {
                current = cornerBlock.getRelative(BlockFace.UP, i);
                for (int j = 0; j < width; j++) {
                    current.setType(Material.PORTAL, data, false);
                    current = current.getRelative(direction);
                }
            }
        }

        return success;
    }

    /**
     * Get the air block in the corner of the portal.
     *
     * @param block The block to start checking for the position.
     * @return The position and direction of the corner of a portal. This position will always be at the bottom.
     */
    private Pair<GlowBlock, BlockFace> getPortalPosition(GlowBlock block) {
        int heightDown = getPortalLengthDirection(block, BlockFace.DOWN) - 1;
        int widthSouth = getPortalLengthDirection(block, BlockFace.SOUTH) - 1;
        int widthWest = getPortalLengthDirection(block, BlockFace.WEST) - 1;

        System.out.println("heigthDown: " + heightDown + " widthSouth: " + widthSouth + " widthWest: " + widthWest);

        if (heightDown != -1 && widthSouth != -1) {
            GlowBlock blockPosition = block.getRelative(BlockFace.DOWN, heightDown);
            blockPosition = blockPosition.getRelative(BlockFace.SOUTH, widthSouth);
            return Pair.of(blockPosition, BlockFace.NORTH);
        } else if (heightDown != -1 && widthWest != -1) {
            GlowBlock blockPosition = block.getRelative(BlockFace.DOWN, heightDown);
            blockPosition = blockPosition.getRelative(BlockFace.WEST, widthSouth);
            return Pair.of(blockPosition, BlockFace.EAST);
        }

        return null;
    }

    /**
     * Get the length from the current block to the first obsidian block. If the first block it encounters is not
     * obsidian or the length exceeds the portal size it is invalid. The given block should not be an obsidian block,
     * but always an air block.
     *
     * @param block The block to start the measurement from.
     * @param direction The direction to check for the length.
     * @return The length to the first obsidian block in the given direction.
     */
    private int getPortalLengthDirection(GlowBlock block, BlockFace direction) {
        int size = 0;

        while (size <= PORTAL_SIZE  && block.getType() == Material.AIR) {
            size++;
            block = block.getRelative(direction);

            if (block.getType() != Material.AIR && block.getType() != Material.OBSIDIAN || size==21) {
                size = -1;
                break;
            }
        }

        return size;
    }

    /**
     * When a player tries to fire an obsidian block, this method checks if there is a valid nether portal and fills it
     * with portal blocks.
     * @param target The block that is clicked on.
     * @param face The face that is clicked on (The vertical direction).
     */
    private void fireNetherPortal(GlowBlock target, BlockFace face) {
        target = target.getRelative(face);
        isValidPortal(target);

    }

    private void fireTnt(GlowBlock tnt,GlowPlayer player) {
        BlockTnt.igniteBlock(tnt, false, player);
    }

    private boolean setBlockOnFire(GlowPlayer player, GlowBlock clicked, BlockFace face,
        ItemStack holding, Vector clickedLoc, EquipmentSlot hand) {
        GlowBlock fireBlock = clicked.getRelative(face);
        if (fireBlock.getType() != Material.AIR) {
            return true;
        }

        if (!clicked.isFlammable()
            && clicked.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
            return true;
        }

        BlockIgniteEvent event = EventFactory.getInstance()
            .callEvent(new BlockIgniteEvent(fireBlock, IgniteCause.FLINT_AND_STEEL, player, null));
        if (event.isCancelled()) {
            player.setItemInHand(holding);
            return false;
        }

        // clone holding to avoid decreasing of the item's amount
        ItemTable.instance().getBlock(Material.FIRE)
            .rightClickBlock(player, clicked, face, holding.clone(), clickedLoc, hand);

        return true;
    }
}
