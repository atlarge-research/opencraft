package science.atlarge.opencraft.opencraft.dispenser;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.ItemTable;
import science.atlarge.opencraft.opencraft.block.blocktype.BlockDispenser;
import science.atlarge.opencraft.opencraft.block.blocktype.BlockLiquid;
import science.atlarge.opencraft.opencraft.block.blocktype.BlockType;
import science.atlarge.opencraft.opencraft.block.entity.state.GlowDispenser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EmptyBucketDispenseBehavior extends DefaultDispenseBehavior {

    @Override
    protected ItemStack dispenseStack(GlowBlock block, ItemStack stack) {
        GlowDispenser dispenser = (GlowDispenser) block.getState();
        GlowBlock target = block.getRelative(BlockDispenser.getFacing(block));
        BlockLiquid liquid = collectableLiquidAtBlock(target);
        if (liquid == null) {
            return super.dispenseStack(block, stack);
        }
        Material bucket = liquid.getBucketType();
        target.setType(Material.AIR);
        stack.setAmount(stack.getAmount() - 1);
        if (stack.getAmount() == 0) {
            stack.setAmount(1);
            stack.setType(bucket);
        } else {
            ItemStack toPlace = new ItemStack(bucket);
            ItemStack remaining = dispenser.placeInDispenser(toPlace);
            if (remaining != null) {
                INSTANCE.dispense(block, remaining);
            }
        }

        return stack;
    }

    private BlockLiquid collectableLiquidAtBlock(GlowBlock target) {
        Material material = target.getType();
        if (material == null || material == Material.AIR) {
            return null;
        }

        BlockType type = ItemTable.instance().getBlock(material);
        if (!(type instanceof BlockLiquid)) {
            return null;
        }

        BlockLiquid liquid = (BlockLiquid) type;
        if (!liquid.isCollectible(target.getState())) {
            return null;
        }

        return liquid;
    }
}
