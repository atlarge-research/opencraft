package science.atlarge.opencraft.opencraft.io.entity;

import science.atlarge.opencraft.opencraft.entity.monster.GlowWither;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.entity.EntityType;

public class WitherStore extends MonsterStore<GlowWither> {

    public WitherStore() {
        super(GlowWither.class, EntityType.WITHER, GlowWither::new);
    }

    @Override
    public void load(GlowWither entity, CompoundTag tag) {
        super.load(entity, tag);
        entity.setInvulnerableTicks(tag.getInt("Invul"));
    }

    @Override
    public void save(GlowWither entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putInt("Invul", entity.getInvulnerableTicks());
    }
}
