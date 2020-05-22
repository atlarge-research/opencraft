package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemBanner extends ItemType {

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face,
        ItemStack holding, Vector clickedLoc, EquipmentSlot hand) {

        ItemTable instance = ItemTable.instance();
        if (face == BlockFace.UP) {
            setPlaceAs(instance.getBlock(Material.STANDING_BANNER));
        } else if (face != BlockFace.DOWN) {
            setPlaceAs(instance.getBlock(Material.WALL_BANNER));
        } else {
            return;
        }

        super.rightClickBlock(player, target, face, holding, clickedLoc, hand);
    }
}
