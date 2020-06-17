package net.glowstone.block.itemtype;

import java.util.ArrayList;
import java.util.List;
import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockTnt;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemFlintAndSteel extends ItemTool {

    private static final int INNER_PORTAL_SIZE = 21;

    public ItemFlintAndSteel() {
        this.setPlaceAs(Material.FIRE);
    }

    @Override
    public boolean onToolRightClick(GlowPlayer player, GlowBlock target, BlockFace face,
        ItemStack holding, Vector clickedLoc, EquipmentSlot hand) {
        if (target.getType() == Material.OBSIDIAN) {
            fireNetherPortal(target, face, player);
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

    /**
     * When a player tries to fire an obsidian block, this method checks if there is a valid nether portal in either
     * the x or z axis.
     *
     * @param target The block that is clicked on.
     * @param face The face that is clicked on (The vertical direction).
     */
    private void fireNetherPortal(GlowBlock target, BlockFace face, GlowPlayer player) {
        target = target.getRelative(face);
        boolean portalNorth = createValidPortal(target, BlockFace.NORTH, player);
        if (!portalNorth) {
            createValidPortal(target, BlockFace.EAST, player);
        }
    }

    /**
     * This method tries to create a valid portal. First it has to check whether the obsidian is placed in the
     * correct place and then it has to fill in the portal with portal blocks if it can.
     *
     * @param block The block where the portal may be placed.
     * @param direction The direction to create the portal in.
     * @return whether the portal was successfully created.
     */
    private boolean createValidPortal(GlowBlock block, BlockFace direction, GlowPlayer player) {

        GlowBlock cornerBlock = getPortalPosition(block, direction);
        if (cornerBlock == null) {
            return false;
        }

        int width = getPortalLengthDirection(cornerBlock, direction);
        int height = getPortalLengthDirection(cornerBlock, BlockFace.UP);
        if (width < 2 || height < 3) {
            return false;
        }

        GlowBlock blockFloor = cornerBlock.getRelative(BlockFace.DOWN);
        GlowBlock blockCeiling = cornerBlock.getRelative(BlockFace.UP, height);
        if (!hasHorizontalPortal(blockFloor, direction, width)
                || !hasHorizontalPortal(blockCeiling, direction, width)) {
            return false;
        }

        if (!hasSidesPortal(cornerBlock, direction, width, height)) {
            return false;
        }

        if (checkInsidePortal(cornerBlock, direction, width, height)) {
            fillPortal(cornerBlock, direction, width, height, player);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if the portal in the horizontal direction is correct. This method is used for checking if the top and
     * bottom of the portal consists completely of obsidian blocks along the width of the portal.
     *
     * @param block The block to start checking from.
     * @param direction The horizontal direction to build the portal in.
     * @param width The amount of blocks to check in the direction of the portal.
     * @return Whether the complete horizontal length of either the top or bottom of the portal consists of obsidian
     *         blocks.
     */
    private boolean hasHorizontalPortal(GlowBlock block, BlockFace direction, int width) {
        for (int i = 0; i < width; i++) {
            if (block.getType() != Material.OBSIDIAN) {
                return false;
            }
            block = block.getRelative(direction);
        }

        return true;
    }

    /**
     * Check if both sides of the portal completely consist of obsidian blocks.
     *
     * @param cornerBlock The block in the bottom corner of the portal to start checking from.
     * @param direction The horizontal direction to build the portal in.
     * @param width The width of the portal.
     * @param height The height of the portal.
     * @return Whether the sides of the portal are completely made of obsidian blocks.
     */
    private boolean hasSidesPortal(GlowBlock cornerBlock, BlockFace direction, int width, int height) {
        GlowBlock sideBlock = cornerBlock.getRelative(direction.getOppositeFace());
        GlowBlock oppositeSideBlock = cornerBlock.getRelative(direction, width);
        for (int i = 0; i < height; i++) {
            if (sideBlock.getType() != Material.OBSIDIAN) {
                return false;
            } else if (oppositeSideBlock.getType() != Material.OBSIDIAN) {
                return false;
            }
            sideBlock = sideBlock.getRelative(BlockFace.UP);
            oppositeSideBlock = oppositeSideBlock.getRelative(BlockFace.UP);
        }

        return true;
    }

    /**
     * Check if the inside of the portal is not obstructed by other blocks.
     *
     * @param cornerBlock The block in a bottom corner of the portal.
     * @param direction The horizontal direction to build the portal in.
     * @param width The width of the inside of the portal
     * @param height The height of the inside of the portal.
     * @return Whether the inside of the portal only contains air or not.
     */
    private boolean checkInsidePortal(GlowBlock cornerBlock, BlockFace direction, int width, int height) {
        GlowBlock current;
        boolean success = true;

        for (int i = 0; i < height; i++) {

            current = cornerBlock.getRelative(BlockFace.UP, i);
            for (int j = 0; j < width; j++) {
                if (current.getType() != Material.AIR) {
                    success = false;
                    break;
                }
                current = current.getRelative(direction);
            }

            if (!success) {
                break;
            }
        }

        return success;
    }

    /**
     * Fill the inside of the portal with portal blocks.
     *
     * @param cornerBlock The block in a bottom corner of the portal.
     * @param direction The horizontal direction to build the portal in.
     * @param width The width of the inside of the portal
     * @param height The height of the inside of the portal.
     * @param player The player that activated the portal.
     */
    private void fillPortal(GlowBlock cornerBlock, BlockFace direction, int width, int height, GlowPlayer player) {

        byte data;
        if (direction == BlockFace.EAST || direction == BlockFace.WEST) {
            data = 1;
        } else {
            data = 2;
        }

        GlowBlock current;
        List<BlockState> blocks = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            current = cornerBlock.getRelative(BlockFace.UP, i);
            for (int j = 0; j < width; j++) {
                current.setType(Material.PORTAL, data, false);
                blocks.add(current.getState());
                current = current.getRelative(direction);
            }
        }

        EntityCreatePortalEvent event = new EntityCreatePortalEvent(player, blocks, PortalType.NETHER);
        event = EventFactory.getInstance().callEvent(event);
        if (!event.isCancelled()) {
            for (BlockState state : blocks) {
                state.update(true);
            }
        }
    }

    /**
     * Get the air block in the inside corner of the portal.
     *
     * @param block The block to start checking for the position.
     * @param direction The direction to try and build the portal in, we need to move in the opposite direction to
     *        start at the correct corner.
     * @return The position of the corner of a portal. This position will always be at the bottom.
     */
    private GlowBlock getPortalPosition(GlowBlock block, BlockFace direction) {

        BlockFace oppositeDirection = direction.getOppositeFace();
        int heightDown = getPortalLengthDirection(block, BlockFace.DOWN) - 1;
        int width = getPortalLengthDirection(block, oppositeDirection) - 1;

        if (heightDown >= 0 && width >= 0) {
            GlowBlock blockPosition = block.getRelative(BlockFace.DOWN, heightDown);
            blockPosition = blockPosition.getRelative(oppositeDirection, width);
            return blockPosition;
        }

        return null;
    }

    /**
     * Get the length from the current block to the first obsidian block. If the first block it encounters is not
     * obsidian or the length exceeds the portal size, the portal is invalid. The given block should not be an obsidian
     * block, but always an air block.
     *
     * @param block The air block to start the measurement from.
     * @param direction The direction to check for the length.
     * @return The length from the start block to the first obsidian block in the provided direction. The starting
     *         block is included in the length.
     */
    private int getPortalLengthDirection(GlowBlock block, BlockFace direction) {

        int length = 0;

        while (length <= INNER_PORTAL_SIZE && block.getType() == Material.AIR) {
            length++;
            block = block.getRelative(direction);

            if (block.getType() != Material.AIR && block.getType() != Material.OBSIDIAN
                    || length == INNER_PORTAL_SIZE + 1) {
                length = -1;
                break;
            }
        }

        return length;
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
