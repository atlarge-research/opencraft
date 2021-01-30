package science.atlarge.opencraft.opencraft.dispenser;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import org.bukkit.inventory.ItemStack;

public interface DispenseBehavior {

    ItemStack dispense(GlowBlock block, ItemStack stack);
}
