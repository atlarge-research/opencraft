package net.glowstone.block.blocktype;

import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Collection;

public class BlockIce extends BlockType {

    @Override
    public Collection<ItemStack> getDrops(GlowBlock me, ItemStack tool) {
        return BlockDropless.EMPTY_STACK;
    }

    @Override
    public boolean canTickRandomly() {
        return true;
    }

    @Override
    public void updateBlock(GlowBlock block) {
        if (block.getLightFromBlocks() > 11 - block.getMaterialValues().getLightOpacity()) {
            final Material type = block.getWorld().getEnvironment() == Environment.NETHER ? Material.AIR : Material.STATIONARY_WATER;
            final GlowBlockState state = block.getState();
            state.setType(type);
            state.setData(new MaterialData(type));
            BlockFadeEvent fadeEvent = new BlockFadeEvent(block, state);
            EventFactory.callEvent(fadeEvent);
            if (!fadeEvent.isCancelled()) {
                state.update(true);
            }
        }
    }
}
