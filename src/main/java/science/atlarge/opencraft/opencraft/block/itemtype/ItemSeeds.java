package science.atlarge.opencraft.opencraft.block.itemtype;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemSeeds extends ItemType {

    private Material cropsType;
    private Material soilType;

    public ItemSeeds(Material cropsType, Material soilType) {
        this.cropsType = cropsType;
        this.soilType = soilType;
    }

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face,
                                ItemStack holding, Vector clickedLoc, EquipmentSlot hand) {
        if (target.getType() == soilType
            && target.getRelative(BlockFace.UP).getType() == Material.AIR && face == BlockFace.UP) {
            GlowBlockState state = target.getRelative(BlockFace.UP).getState();
            state.setType(cropsType);
            state.setRawData((byte) 0);
            state.update(true);

            // deduct from stack if not in creative mode
            if (player.getGameMode() != GameMode.CREATIVE) {
                holding.setAmount(holding.getAmount() - 1);
            }
        }
    }
}
