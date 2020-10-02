package science.atlarge.opencraft.opencraft.block.entity;

import lombok.Getter;
import lombok.Setter;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.block.entity.state.GlowJukebox;
import science.atlarge.opencraft.opencraft.io.nbt.NbtSerialization;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.inventory.ItemStack;

public class JukeboxEntity extends BlockEntity {

    @Getter
    @Setter
    private ItemStack playing;

    public JukeboxEntity(GlowBlock block) {
        super(block);
        setSaveId("minecraft:jukebox");
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        playing =
            tag.containsKey("RecordItem") ? NbtSerialization.readItem(tag.getCompound("RecordItem"))
                : null;
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        tag.putCompound("RecordItem", NbtSerialization.writeItem(playing, 0));
    }

    @Override
    public GlowBlockState getState() {
        return new GlowJukebox(block);
    }
}
