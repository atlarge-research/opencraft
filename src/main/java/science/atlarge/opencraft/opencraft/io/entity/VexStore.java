package science.atlarge.opencraft.opencraft.io.entity;

import science.atlarge.opencraft.opencraft.entity.monster.GlowVex;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.entity.EntityType;

public class VexStore extends MonsterStore<GlowVex> {

    public VexStore() {
        super(GlowVex.class, EntityType.VEX, GlowVex::new);
    }

    @Override
    public void load(GlowVex entity, CompoundTag tag) {
        super.load(entity, tag);
        tag.readInt("LifeTicks", entity::setLifeTicks);
    }

    @Override
    public void save(GlowVex entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putInt("LifeTicks", entity.getLifeTicks());
    }
}
