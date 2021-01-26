package science.atlarge.opencraft.opencraft.io.entity;

import science.atlarge.opencraft.opencraft.entity.GlowHangingEntity;
import science.atlarge.opencraft.opencraft.entity.GlowHangingEntity.HangingFace;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.entity.EntityType;

public abstract class HangingStore<T extends GlowHangingEntity> extends EntityStore<T> {

    public HangingStore(Class<? extends T> clazz, EntityType type) {
        super(clazz, type);
    }

    @Override
    public void load(T entity, CompoundTag tag) {
        super.load(entity, tag);

        tag.readByte("Facing", facing ->
            entity.setFacingDirection(HangingFace.values()[facing].getBlockFace()));
    }

    @Override
    public void save(T entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putByte("Facing", HangingFace.getByBlockFace(entity.getFacing()).ordinal());
    }
}
