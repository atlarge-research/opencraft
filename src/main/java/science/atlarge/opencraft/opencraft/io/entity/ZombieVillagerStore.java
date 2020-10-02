package science.atlarge.opencraft.opencraft.io.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static science.atlarge.opencraft.opencraft.entity.passive.GlowVillager.getRandomProfession;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import science.atlarge.opencraft.opencraft.entity.monster.GlowZombie;
import science.atlarge.opencraft.opencraft.entity.monster.GlowZombieVillager;
import science.atlarge.opencraft.opencraft.entity.passive.GlowVillager;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

public class ZombieVillagerStore extends ZombieStore<GlowZombieVillager> {

    private static final Villager.Profession[] PROFESSIONS = Villager.Profession.values();

    public ZombieVillagerStore() {
        super(GlowZombieVillager.class, EntityType.ZOMBIE_VILLAGER, GlowZombieVillager::new);
    }

    @Override
    public void load(GlowZombie zombie, CompoundTag compound) {
        checkArgument(zombie instanceof GlowZombieVillager);
        GlowZombieVillager entity = (GlowZombieVillager) zombie;
        super.load(entity, compound);
        entity.setVillagerProfession(compound.tryGetInt("Profession")
                .filter(GlowVillager::isValidProfession)
                .map(GlowVillager::getProfessionById)
                .orElseGet(() -> getRandomProfession(ThreadLocalRandom.current())));
        entity.setConversionTime(compound.tryGetInt("ConversionTime").orElse(-1));
        compound.readUuid("ConversionPlayerMost", "ConversionPlayerLeast",
                entity::setConversionPlayer);
    }

    @Override
    public void save(GlowZombie zombie, CompoundTag compound) {
        checkArgument(zombie instanceof GlowZombieVillager);
        GlowZombieVillager entity = (GlowZombieVillager) zombie;
        super.save(entity, compound);

        final Villager.Profession profession = entity.getVillagerProfession();
        if (profession != null && profession != Villager.Profession.HUSK) {
            compound.putInt("Profession", profession.ordinal());
        }

        compound.putInt("ConversionTime", entity.getConversionTime());

        final UUID conversionPlayer = entity.getConversionPlayer();
        if (conversionPlayer != null) {
            compound.putLong("ConversionPlayerMost", conversionPlayer.getMostSignificantBits());
            compound.putLong("ConversionPlayerLeast", conversionPlayer.getLeastSignificantBits());
        }
    }
}
