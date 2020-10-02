package science.atlarge.opencraft.opencraft.block.entity;

import lombok.Getter;
import lombok.Setter;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.block.entity.state.GlowBed;
import science.atlarge.opencraft.opencraft.constants.GlowBlockEntity;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;

public class BedEntity extends BlockEntity {

    @Getter
    @Setter
    private int color;

    public BedEntity(GlowBlock block) {
        super(block);
        setSaveId("minecraft:bed");
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        color = tag.getInt("color");
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        tag.putInt("color", color);
    }

    @Override
    public GlowBlockState getState() {
        return new GlowBed(block);
    }

    @Override
    public void update(GlowPlayer player) {
        super.update(player);
        CompoundTag nbt = new CompoundTag();
        GlowWorld world = player.getWorld();
        saveNbt(nbt);
        // TODO: it is possible that this causes a broadcast message to be sent multiple times.
        world.sendBlockEntityChange(block.getLocation(), GlowBlockEntity.BED, nbt);
    }
}
