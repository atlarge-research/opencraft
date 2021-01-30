package science.atlarge.opencraft.opencraft.io.entity;

import java.util.function.Function;
import science.atlarge.opencraft.opencraft.entity.projectile.GlowSplashPotion;
import science.atlarge.opencraft.opencraft.io.nbt.NbtSerialization;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.Location;
import org.jetbrains.annotations.NonNls;

public class SplashPotionStore<T extends GlowSplashPotion> extends ProjectileStore<T> {
    public SplashPotionStore(Class<T> clazz, @NonNls String id, Function<Location, T> constructor) {
        super(clazz, id, constructor);
    }

    @Override
    public void save(T entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putCompound("Potion", NbtSerialization.writeItem(entity.getItem(), -1));
    }

    @Override
    public void load(T entity, CompoundTag tag) {
        super.load(entity, tag);
        tag.readItem("Potion", entity::setItem);
    }
}
