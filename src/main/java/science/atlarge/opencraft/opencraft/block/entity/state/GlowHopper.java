package science.atlarge.opencraft.opencraft.block.entity.state;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.entity.HopperEntity;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;

public class GlowHopper extends GlowContainer implements Hopper {

    public GlowHopper(GlowBlock block) {
        super(block);
    }

    private HopperEntity getBlockEntity() {
        return (HopperEntity) getBlock().getBlockEntity();
    }

    @Override
    public Inventory getInventory() {
        return getBlockEntity().getInventory();
    }
}
