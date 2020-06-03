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

    private static final int PORTAL_SIZE = 20;

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
        boolean portalSouth = createValidPortal(target, BlockFace.NORTH, player);
        if (portalSouth) {
            return;
        }

        createValidPortal(target, BlockFace.EAST, player);

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

        int height = getPortalLengthDirection(cornerBlock, BlockFace.UP);
        int width = getPortalLengthDirection(cornerBlock, direction);
        if (height < 3 || width < 2) {
            return false;
        }

        GlowBlock blockFloor = cornerBlock.getRelative(BlockFace.DOWN);
        for (int i = 0; i < width; i++) {
            if (blockFloor.getType() != Material.OBSIDIAN) {
                return false;
            }
            blockFloor = blockFloor.getRelative(direction);
        }

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

        GlowBlock blockCeiling = cornerBlock.getRelative(BlockFace.UP, height);
        for (int i = 0; i < width; i++) {
            if (blockCeiling.getType() != Material.OBSIDIAN) {
                return false;
            }
            blockCeiling = blockCeiling.getRelative(direction);
        }

        return fillPortal(height, width, cornerBlock, direction, player);
    }

    /**
     * Check if the inside of the portal is not obstructed by other blocks and then fill it with portal blocks.
     *
     * @param height The height of the inside of the portal
     * @param width The width of the inside of the portal
     * @param cornerBlock The block in a bottom corner of the portal.
     * @param direction The horizontal direction to build the portal in.
     * @return Whether the portal was succesfully activated or not.
     */
    private boolean fillPortal(int height, int width, GlowBlock cornerBlock, BlockFace direction, GlowPlayer player) {
        GlowBlock current;
        List<BlockState> blocks = new ArrayList<>();
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

        byte data;
        if (direction == BlockFace.EAST || direction == BlockFace.WEST) {
            data = 1;
        } else {
            data = 2;
        }

        if (success) {
            for (int i = 0; i < height; i++) {
                current = cornerBlock.getRelative(BlockFace.UP, i);
                for (int j = 0; j < width; j++) {
                    current.setType(Material.PORTAL, data, false);
                    blocks.add(current.getState());
                    current = current.getRelative(direction);
                }
            }
        }

        if (!EventFactory.getInstance()
                .callEvent(new EntityCreatePortalEvent(player, blocks, PortalType.NETHER))
                .isCancelled()) {
            for (BlockState state : blocks) {
                state.update(true);
            }
        }

        return success;
    }

    /**
     * Get the air block in the inside corner of the portal.
     *
     * @param block The block to start checking for the position.
     * @param direction The direction to try and build the portal in, we need to move in the opposite direction to
     *                  start at the correct corner.
     * @return The position of the corner of a portal. This position will always be at the bottom.
     */
    private GlowBlock getPortalPosition(GlowBlock block, BlockFace direction) {
        BlockFace oppositeDirection = direction.getOppositeFace();
        int heightDown = getPortalLengthDirection(block, BlockFace.DOWN) - 1;
        int width = getPortalLengthDirection(block, oppositeDirection) - 1;

        if (heightDown != -1 && width != -1) {
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

        while (length <= PORTAL_SIZE  && block.getType() == Material.AIR) {
            length++;
            block = block.getRelative(direction);

            if (block.getType() != Material.AIR && block.getType() != Material.OBSIDIAN || length == 21) {
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
