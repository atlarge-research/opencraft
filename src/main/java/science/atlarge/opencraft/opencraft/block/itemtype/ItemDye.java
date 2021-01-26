package science.atlarge.opencraft.opencraft.block.itemtype;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.ItemTable;
import science.atlarge.opencraft.opencraft.block.blocktype.BlockType;
import science.atlarge.opencraft.opencraft.block.blocktype.IBlockGrowable;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class ItemDye extends ItemType {

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face,
        ItemStack holding, Vector clickedLoc, EquipmentSlot hand) {
        MaterialData data = holding.getData();
        if (data instanceof Dye) {
            Dye dye = (Dye) data;

            if (dye.getColor() == DyeColor.WHITE && player.getGameMode()
                != GameMode.ADVENTURE) { // player interacts with bone meal in hand
                BlockType blockType = ItemTable.instance().getBlock(target.getType());
                if (blockType instanceof IBlockGrowable) {
                    IBlockGrowable growable = (IBlockGrowable) blockType;
                    if (growable.isFertilizable(target)) {
                        // spawn some green particles
                        target.getWorld()
                            .playEffect(target.getLocation(), Effect.HAPPY_VILLAGER, 0);

                        if (growable.canGrowWithChance(target)) {
                            growable.grow(player, target);
                        }

                        // deduct from stack if not in creative mode
                        if (player.getGameMode() != GameMode.CREATIVE) {
                            holding.setAmount(holding.getAmount() - 1);
                        }
                    }
                }
            } else if (dye.getColor() == DyeColor.BROWN && target.getType() == Material.LOG) {
                ItemTable.instance().getBlock(Material.COCOA)
                    .rightClickBlock(player, target, face, holding, clickedLoc, hand);
            }
        }
    }
}
