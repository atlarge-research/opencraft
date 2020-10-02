package science.atlarge.opencraft.opencraft.block.entity;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.block.entity.state.GlowDropper;

public class DropperEntity extends DispenserEntity {

    public DropperEntity(GlowBlock block) {
        super(block);
    }

    @Override
    protected void setOwnSaveId() {
        setSaveId("minecraft:dropper");
    }

    @Override
    public GlowBlockState getState() {
        return new GlowDropper(block);
    }
}
