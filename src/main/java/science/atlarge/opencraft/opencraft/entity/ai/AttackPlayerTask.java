package science.atlarge.opencraft.opencraft.entity.ai;

import science.atlarge.opencraft.opencraft.entity.GlowLivingEntity;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Hits the nearest player in reach, once per second.
 */
public class AttackPlayerTask extends EntityTask {

    static final double REACH = 1.5;
    static final int COOLDOWN_TICKS = 20;

    private int cooldown = 0;

    public AttackPlayerTask() {
        super("attack_player");
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public int getDurationMin() {
        return 0;
    }

    @Override
    public int getDurationMax() {
        return 0;
    }

    @Override
    public boolean shouldStart(GlowLivingEntity entity) {
        return false;
    }

    @Override
    public void start(GlowLivingEntity entity) {
    }

    @Override
    public void end(GlowLivingEntity entity) {
    }

    @Override
    public void execute(GlowLivingEntity entity) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        Difficulty difficulty = entity.getWorld().getDifficulty();
        if (difficulty == Difficulty.PEACEFUL) {
            return;
        }
        Location location = entity.getLocation();
        for (Entity nearby : entity.getNearbyEntities(REACH, REACH, REACH)) {
            if (!(nearby instanceof Player)) {
                continue;
            }
            Player player = (Player) nearby;
            GameMode mode = player.getGameMode();
            if (mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR) {
                continue;
            }
            if (player.getLocation().distanceSquared(location) > REACH * REACH) {
                continue;
            }
            player.damage(damageFor(difficulty), entity, DamageCause.ENTITY_ATTACK);
            cooldown = COOLDOWN_TICKS;
            return;
        }
    }

    static double damageFor(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 2;
            case HARD:
                return 4.5;
            default:
                return 3;
        }
    }
}
