package science.atlarge.opencraft.opencraft.io.entity;

import java.util.function.Function;
import science.atlarge.opencraft.opencraft.entity.passive.GlowAbstractHorse;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class AbstractHorseStore<T extends GlowAbstractHorse> extends TameableStore<T> {

    public AbstractHorseStore(Class<T> clazz, EntityType type,
            Function<Location, ? extends T> creator) {
        super(clazz, type, creator);
    }

    @Override
    public void load(T entity, CompoundTag compound) {
        super.load(entity, compound);
        entity.setTamed(compound.getBoolean("Tame", false));
    }

    @Override
    public void save(T entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putBool("Tame", entity.isTamed());
    }
}
