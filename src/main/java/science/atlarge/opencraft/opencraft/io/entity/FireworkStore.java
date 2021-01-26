package science.atlarge.opencraft.opencraft.io.entity;

import science.atlarge.opencraft.opencraft.entity.passive.GlowFirework;
import science.atlarge.opencraft.opencraft.io.nbt.NbtSerialization;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class FireworkStore extends EntityStore<GlowFirework> {

    public FireworkStore() {
        super(GlowFirework.class, EntityType.FIREWORK);
    }

    @Override
    public GlowFirework createEntity(Location location, CompoundTag compound) {
        return new GlowFirework(location, null, null, null);
    }

    @Override
    public void load(GlowFirework entity, CompoundTag tag) {
        super.load(entity, tag);
        tag.readInt("Life", entity::setTicksLived);
        tag.readInt("LifeTime", entity::setLifeTime);
        tag.readItem("FireworksItem", entity::setFireworkItem);
        tag.readUuid("SpawningEntityMost", "SpawningEntityLeast", entity::setSpawningEntity);
    }

    @Override
    public void save(GlowFirework entity, CompoundTag tag) {
        super.save(entity, tag);

        tag.putInt("Life", entity.getTicksLived());
        tag.putInt("LifeTime", entity.getLifeTime());
        CompoundTag fireworkItem = NbtSerialization.writeItem(entity.getFireworkItem(), -1);
        tag.putCompound("FireworksItem", fireworkItem);

        tag.putLong("SpawningEntityMost", entity.getSpawningEntity().getMostSignificantBits());
        tag.putLong("SpawningEntityLeast", entity.getSpawningEntity().getLeastSignificantBits());
    }
}
