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

    /**
     * Retrieve the horizontal direction the portal should be made in. This method assumes a portal is 2 wide. We
     * check on which side of the block an obsidian block is, the opposite direction is the direction the portal
     * should be made in. This method also checks if there is a valid portal in one of the horizontal directions.
     * @param block The block in a portal.
     * @param verticalFace The vertical direction the portal should be created in.
     * @return The direction of the portal.
     */
    private BlockFace getHorizontalPortalDirection(GlowBlock block, BlockFace verticalFace) {

        List<BlockFace> horizontal = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);

        for (BlockFace direction : horizontal) {
            if (block.getRelative(direction).getType() == Material.OBSIDIAN) {
                BlockFace opposite = direction.getOppositeFace();
                if (isPortal(block, verticalFace, opposite)) {
                    return opposite;
                }
            }
        }

        return null;
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
        if (height == -1 || height < 3 || width == -1 || width < 2) {
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
        GlowBlock oppositeSideBlock = cornerBlock.getRelative(direction, width + 1);
        for (int i = 0; i < height; i++){
            if (sideBlock.getType() != Material.OBSIDIAN) {
                return false;
            } else if (oppositeSideBlock.getType() != Material.OBSIDIAN) {
                return false;
            }
            sideBlock = sideBlock.getRelative(BlockFace.UP);
            oppositeSideBlock = oppositeSideBlock.getRelative(BlockFace.UP);
        }

        //GlowBlock blockCeiling = cornerBlock.getRelative()

        return false;
    }

    /**
     * Get the air block in the corner of the portal.
     *
     * @param block The block to start checking for the position.
     * @return The position and direction of the corner of a portal. This position will always be at the bottom.
     */
    private Pair<GlowBlock, BlockFace> getPortalPosition(GlowBlock block) {
        int heightDown = getPortalLengthDirection(block, BlockFace.DOWN);
        int widthSouth = getPortalLengthDirection(block, BlockFace.SOUTH);
        int widthWest = getPortalLengthDirection(block, BlockFace.WEST);

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
        int size = -1;

        while (size < PORTAL_SIZE  && block.getType() == Material.AIR) {
            size++;
            block = block.getRelative(direction);

            if (block.getType() != Material.AIR || block.getType() != Material.OBSIDIAN) {
                size = -1;
                break;
            }
        }

        return size;
    }

    /**
     * Checks if there is a 2x3 portal. A portal should be surrounded by obsidian for it to work.
     * @param block A block in a corner of the portal.
     * @param verticalFace Whether we have to build the portal up or down.
     * @param direction The horizontal direction to check for a portal.
     * @return If this is a valid portal.
     */
    private boolean isPortal(GlowBlock block, BlockFace verticalFace, BlockFace direction) {

        // TODO: Support portals of various sizes.
        if (direction == null) {
            return false;
        }

        BlockFace oppositeVerticalFace = verticalFace.getOppositeFace();
        GlowBlock oppositeVerticalBlock = block.getRelative(oppositeVerticalFace);
        GlowBlock sideBlock = oppositeVerticalBlock.getRelative(direction);
        if (oppositeVerticalBlock.getType() != Material.OBSIDIAN || sideBlock.getType() != Material.OBSIDIAN) {
            return false;
        }

        for (int i = 1; i <= 3; i++) {

            if (block.getRelative(direction, 2).getType() != Material.OBSIDIAN) {
                return false;
            }

            BlockFace oppositeDirection = direction.getOppositeFace();
            if (block.getRelative(oppositeDirection).getType() != Material.OBSIDIAN) {
                return false;
            }

            block = block.getRelative(verticalFace);
        }

        if (block.getType() != Material.OBSIDIAN || block.getRelative(direction).getType() != Material.OBSIDIAN) {
            return false;
        }

        return true;
    }

    /**
     * When a player tries to fire an obsidian block, this method checks if there is a valid nether portal and fills it
     * with portal blocks.
     * @param target The block that is clicked on.
     * @param face The face that is clicked on (The vertical direction).
     */
    private void fireNetherPortal(GlowBlock target, BlockFace face) {
        if (face == BlockFace.UP || face == BlockFace.DOWN) {

            target = target.getRelative(face);
            BlockFace direction = getHorizontalPortalDirection(target, face);
            if (direction == null) {
                return;
            }

            GlowBlock sideBlock = target.getRelative(direction);

            byte data;
            if (direction == BlockFace.EAST || direction == BlockFace.WEST) {
                data = 1;
            } else {
                data = 2;
            }

            // TODO: Support portals of various sizes.
            while (target.getType() == Material.AIR) {
                target.setType(Material.PORTAL, data, false);
                sideBlock.setType(Material.PORTAL, data, false);
                target = target.getRelative(face);
                sideBlock = sideBlock.getRelative(face);
            }

        }
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
