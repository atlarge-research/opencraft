package science.atlarge.opencraft.opencraft.block.entity;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.block.entity.state.GlowHopper;
import science.atlarge.opencraft.opencraft.inventory.GlowInventory;
import org.bukkit.event.inventory.InventoryType;

public class HopperEntity extends ContainerEntity {

    public HopperEntity(GlowBlock block) {
        super(block, new GlowInventory(new GlowHopper(block), InventoryType.HOPPER));
        setSaveId("minecraft:hopper");
    }

    @Override
    public GlowBlockState getState() {
        return new GlowHopper(block);
    }
}
