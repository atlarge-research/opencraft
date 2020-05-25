package net.glowstone.block.itemtype;

import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockTnt;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemFlintAndSteel extends ItemTool {

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
     * should be made in.
     * @param block The block in a portal
     * @return The direction of the portal.
     */
    private BlockFace getHorizontalPortalDirection(GlowBlock block) {
        if (block.getRelative(BlockFace.NORTH).getType() == Material.OBSIDIAN) {
            return BlockFace.SOUTH;
        } else if (block.getRelative(BlockFace.EAST).getType() == Material.OBSIDIAN) {
            return BlockFace.WEST;
        } else if (block.getRelative(BlockFace.SOUTH).getType() == Material.OBSIDIAN) {
            return BlockFace.NORTH;
        } else if (block.getRelative(BlockFace.WEST).getType() == Material.OBSIDIAN) {
            return BlockFace.EAST;
        }
        return null;
    }

    /**
     * Checks if there is a 2x3 portal. A portal should be surrounded by obsidian for it to work.
     * @param block A block in a corner of the portal
     * @param verticalFace Whether we have to build the portal up or down.
     * @return If this is a valid portal.
     */
    private boolean isPortal(GlowBlock block, BlockFace verticalFace) {
        // TODO: Support portals of various sizes.
        Material obsidian = Material.OBSIDIAN;
        BlockFace direction = getHorizontalPortalDirection(block);
        if (direction == null) {
            return false;
        }

        BlockFace oppositeFace = verticalFace.getOppositeFace();
        GlowBlock oppositeBlock = block.getRelative(oppositeFace);
        if (oppositeBlock.getType() != obsidian || oppositeBlock.getRelative(direction).getType() != obsidian) {
            return false;
        }

        for (int i = 1; i <= 3; i++) {

            if (block.getRelative(direction, 2).getType() != obsidian) {
                return false;
            }

            BlockFace oppositeDirection = direction.getOppositeFace();
            if (block.getRelative(oppositeDirection).getType() != obsidian) {
                return false;
            }

            block = block.getRelative(verticalFace);
        }

        if (block.getType() != obsidian || block.getRelative(direction).getType() != obsidian) {
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
            if (!isPortal(target, face)) {
                return;
            }

            BlockFace direction = getHorizontalPortalDirection(target);
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
