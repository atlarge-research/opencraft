package science.atlarge.opencraft.opencraft.block.itemtype;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.ItemTable;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
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
