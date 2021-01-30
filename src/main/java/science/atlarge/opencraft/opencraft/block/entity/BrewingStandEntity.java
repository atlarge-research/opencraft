package science.atlarge.opencraft.opencraft.block.entity;

import lombok.Getter;
import lombok.Setter;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.block.entity.state.GlowBrewingStand;
import science.atlarge.opencraft.opencraft.inventory.GlowBrewerInventory;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;

public class BrewingStandEntity extends ContainerEntity {

    @Getter
    @Setter
    private int brewTime;

    public BrewingStandEntity(GlowBlock block) {
        super(block, new GlowBrewerInventory(new GlowBrewingStand(block, 0)));
        setSaveId("minecraft:brewing_stand");
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        tag.readInt("BrewTime", this::setBrewTime);
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        tag.putInt("BrewTime", brewTime);
    }

    @Override
    public GlowBlockState getState() {
        return new GlowBrewingStand(block);
    }
}
