package net.glowstone.entity.monster;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;

import java.util.Random;
import java.util.UUID;

public class GlowPigZombie extends GlowZombie implements PigZombie {

    private int anger;
    private UUID hurtBy;

    public GlowPigZombie(Location loc) {
        super(loc, EntityType.PIG_ZOMBIE);
        setMaxHealthAndHealth(20);
    }

    @Override
    public int getAnger() {
        return anger;
    }

    @Override
    public void setAnger(int level) {
        this.anger = level;
    }

    @Override
    public void setAngry(boolean angry) {
        if (!angry) anger = 0;
        else if (isAngry()) anger = ((int) (new Random().nextGaussian() * 400)) + 400;
    }

    @Override
    public boolean isAngry() {
        return anger > 0;
    }

    public UUID getHurtBy() {
        return hurtBy;
    }

    public void setHurtBy(UUID hurtBy) {
        this.hurtBy = hurtBy;
    }
}
