package science.atlarge.opencraft.opencraft.io.entity;

import java.util.function.Function;
import science.atlarge.opencraft.opencraft.entity.monster.GlowSlime;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

class SlimeStore<T extends GlowSlime> extends MonsterStore<T> {

    public SlimeStore(Class<T> clazz, EntityType type, Function<Location, ? extends T> creator) {
        super(clazz, type, creator);
    }

    @Override
    public void load(T entity, CompoundTag tag) {
        super.load(entity, tag);
        entity.setSize(tag.tryGetInt("Size").orElse(1));
        entity.setOnGround(tag.getBoolean("wasOnGround", false));
    }

    @Override
    public void save(T entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putInt("Size", entity.getSize());
        tag.putBool("wasOnGround", entity.isOnGround());
    }

}
