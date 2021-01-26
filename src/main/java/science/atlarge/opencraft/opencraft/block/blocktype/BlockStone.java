package science.atlarge.opencraft.opencraft.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.inventory.MaterialMatcher;
import science.atlarge.opencraft.opencraft.inventory.ToolType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Stone;
import org.bukkit.material.types.StoneType;

public class BlockStone extends BlockNeedsTool {

    @Override
    protected MaterialMatcher getNeededMiningTool(GlowBlock block) {
        return ToolType.PICKAXE;
    }

    @Override
    public Collection<ItemStack> getMinedDrops(GlowBlock block) {
        if (((Stone) block.getState().getData()).getType() == StoneType.NORMAL) {
            return Arrays.asList(new ItemStack(Material.COBBLESTONE));
        } else {
            return Arrays.asList(new ItemStack(Material.STONE, 1, block.getData()));
        }
    }
}
