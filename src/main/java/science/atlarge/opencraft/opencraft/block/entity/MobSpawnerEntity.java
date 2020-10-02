package science.atlarge.opencraft.opencraft.block.entity;

import lombok.Getter;
import lombok.Setter;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.block.entity.state.GlowCreatureSpawner;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.entity.EntityType;

public class MobSpawnerEntity extends BlockEntity {

    private static final EntityType DEFAULT = EntityType.PIG;

    @Getter
    @Setter
    private EntityType spawning;
    @Getter
    @Setter
    private int delay;

    public MobSpawnerEntity(GlowBlock block) {
        super(block);
        setSaveId("minecraft:mob_spawner");
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        spawning = tag.tryGetString("EntityId").map(EntityType::fromName).orElse(DEFAULT);
        delay = tag.tryGetInt("Delay").orElse(0);
    }

    @Override
    public GlowBlockState getState() {
        return new GlowCreatureSpawner(block);
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        tag.putString("EntityId", spawning == null ? "" : spawning.getName());
        tag.putInt("Delay", delay);
    }
}
