package net.glowstone.block.itemtype;

import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockTNT;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ItemFlintAndSteel extends ItemTool {

    @Override
    public boolean onToolRightClick(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        if (target.getType() == Material.OBSIDIAN) {
            fireNetherPortal(target);
            return true;
        }
        if (target.getType() == Material.TNT) {
            fireTnt(target);
            return true;
        }
        if (target.isFlammable() || target.getType().isOccluding()) {
            setBlockOnFire(player, target, face, holding, clickedLoc);
            return true;
        }
        return false;
    }

    private void fireNetherPortal(GlowBlock target) {
        target = target.getRelative(BlockFace.UP);
        List<Block> portals = new ArrayList<>();
        while (target.getType() == Material.AIR) {
            portals.add(target);
            target = target.getRelative(BlockFace.UP);
        }
        if (target.getType() == Material.OBSIDIAN) {
            for (Block portal : portals) {
                portal.getState().setType(Material.PORTAL);
            }
            for (Block portal : portals) {
                portal.getState().update(true, true);
            }
        }
    }

    private void fireTnt(GlowBlock tnt) {
        BlockTNT.igniteBlock(tnt, false);
    }

    private boolean setBlockOnFire(GlowPlayer player, GlowBlock clicked, BlockFace face, ItemStack holding, Vector clickedLoc) {
        GlowBlock fireBlock = clicked.getRelative(face);
        if (fireBlock.getType() != Material.AIR) {
            return true;
        }

        if (!clicked.isFlammable() && clicked.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
            return true;
        }

        BlockIgniteEvent event = EventFactory.callEvent(new BlockIgniteEvent(fireBlock, IgniteCause.FLINT_AND_STEEL, player, null));
        if (event.isCancelled()) {
            player.setItemInHand(holding);
            return false;
        }

        // clone holding to avoid decreasing of the item's amount
        ItemTable.instance().getBlock(Material.FIRE).rightClickBlock(player, clicked, face, holding.clone(), clickedLoc);

        return true;
    }
}
