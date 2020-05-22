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
        if (face == BlockFace.UP) {
            setPlaceAs(ItemTable.instance().getBlock(Material.STANDING_BANNER));
        } else if (face == BlockFace.DOWN) {
            return;
        } else {
            setPlaceAs(ItemTable.instance().getBlock(Material.WALL_BANNER));
        }
        super.rightClickBlock(player, target, face, holding, clickedLoc, hand);
    }
}
