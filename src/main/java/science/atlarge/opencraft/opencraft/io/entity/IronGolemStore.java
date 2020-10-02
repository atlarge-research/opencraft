package science.atlarge.opencraft.opencraft.io.entity;

import science.atlarge.opencraft.opencraft.entity.monster.GlowIronGolem;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.entity.EntityType;

class IronGolemStore extends MonsterStore<GlowIronGolem> {

    public IronGolemStore() {
        super(GlowIronGolem.class, EntityType.IRON_GOLEM, GlowIronGolem::new);
    }

    @Override
    public void load(GlowIronGolem entity, CompoundTag compound) {
        super.load(entity, compound);
        entity.setPlayerCreated(compound.getBoolean("PlayerCreated", true));
    }

    @Override
    public void save(GlowIronGolem entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putBool("PlayerCreated", entity.isPlayerCreated());
    }
}
