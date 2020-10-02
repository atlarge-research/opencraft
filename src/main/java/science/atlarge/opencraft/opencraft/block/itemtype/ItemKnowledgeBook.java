package science.atlarge.opencraft.opencraft.block.itemtype;

import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.ServerProvider;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.util.Vector;

public class ItemKnowledgeBook extends ItemType {

    public ItemKnowledgeBook() {
        setMaxStackSize(1);
    }

    @Override
    public Context getContext() {
        return Context.ANY;
    }

    @Override
    public void rightClickBlock(
        GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding,
        Vector clickedLoc, EquipmentSlot hand) {
        rightClickBook(player, holding);
    }

    @Override
    public void rightClickAir(GlowPlayer player, ItemStack holding) {
        rightClickBook(player, holding);
    }

    private void rightClickBook(GlowPlayer player, ItemStack holding) {
        if (holding.getItemMeta() instanceof KnowledgeBookMeta) {
            KnowledgeBookMeta recipes = (KnowledgeBookMeta) holding.getItemMeta();
            if (recipes.hasRecipes()) {
                for (NamespacedKey recipe : recipes.getRecipes()) {
                    player.learnRecipe(((GlowServer) ServerProvider.getServer())
                            .getCraftingManager().getRecipeByKey(recipe), true);
                }
            }

            if (player.getGameMode() != GameMode.CREATIVE) {
                holding.setAmount(0);
            }
        }
    }
}
