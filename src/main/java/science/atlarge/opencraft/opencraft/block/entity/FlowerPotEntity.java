package science.atlarge.opencraft.opencraft.block.entity;

import lombok.Getter;
import lombok.Setter;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.block.entity.state.GlowFlowerPot;
import science.atlarge.opencraft.opencraft.constants.GlowBlockEntity;
import science.atlarge.opencraft.opencraft.constants.ItemIds;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.material.MaterialData;

public class FlowerPotEntity extends BlockEntity {
    @Getter
    @Setter
    private MaterialData contents;

    public FlowerPotEntity(GlowBlock block) {
        super(block);
        setSaveId("minecraft:flower_pot");
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        tag.tryGetMaterial("Item").ifPresent(
            item -> this.contents = item.getNewData(
                    (byte) (int) (tag.tryGetInt("Data").orElse(0))));
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);

        if (contents != null) {
            tag.putString("Item", ItemIds.getName(contents.getItemType()));
            tag.putInt("Data", contents.getData());
        } else {
            // Mimics how Minecraft does it.
            tag.putString("Item", "");
            tag.putInt("Data", 0);
        }
    }

    @Override
    public GlowBlockState getState() {
        return new GlowFlowerPot(block);
    }

    @Override
    public void update(GlowPlayer player) {
        super.update(player);
        CompoundTag nbt = new CompoundTag();
        GlowWorld world = player.getWorld();
        saveNbt(nbt);
        // TODO: it is possible that this causes a broadcast message to be sent multiple times.
        world.sendBlockEntityChange(getBlock().getLocation(), GlowBlockEntity.FLOWER_POT, nbt);
    }
}
