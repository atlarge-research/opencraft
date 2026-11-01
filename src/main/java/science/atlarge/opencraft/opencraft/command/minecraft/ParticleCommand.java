package science.atlarge.opencraft.opencraft.command.minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import science.atlarge.opencraft.opencraft.command.CommandUtils;
import science.atlarge.opencraft.opencraft.i18n.LocalizedStringImpl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

/**
 * Spawns particles: {@code /particle <name> <x> <y> <z> <xd> <yd> <zd> <speed> [count] [mode]
 * [player]}. Particles that need extra data, such as {@code iconcrack}, are not supported.
 */
public class ParticleCommand extends GlowVanillaCommand {

    /**
     * The vanilla 1.12 names of the particles, case-insensitive.
     */
    private static final Map<String, Particle> PARTICLES = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        PARTICLES.put("explode", Particle.EXPLOSION_NORMAL);
        PARTICLES.put("largeexplode", Particle.EXPLOSION_LARGE);
        PARTICLES.put("hugeexplosion", Particle.EXPLOSION_HUGE);
        PARTICLES.put("fireworksSpark", Particle.FIREWORKS_SPARK);
        PARTICLES.put("bubble", Particle.WATER_BUBBLE);
        PARTICLES.put("splash", Particle.WATER_SPLASH);
        PARTICLES.put("wake", Particle.WATER_WAKE);
        PARTICLES.put("suspended", Particle.SUSPENDED);
        PARTICLES.put("depthsuspend", Particle.SUSPENDED_DEPTH);
        PARTICLES.put("crit", Particle.CRIT);
        PARTICLES.put("magicCrit", Particle.CRIT_MAGIC);
        PARTICLES.put("smoke", Particle.SMOKE_NORMAL);
        PARTICLES.put("largesmoke", Particle.SMOKE_LARGE);
        PARTICLES.put("spell", Particle.SPELL);
        PARTICLES.put("instantSpell", Particle.SPELL_INSTANT);
        PARTICLES.put("mobSpell", Particle.SPELL_MOB);
        PARTICLES.put("mobSpellAmbient", Particle.SPELL_MOB_AMBIENT);
        PARTICLES.put("witchMagic", Particle.SPELL_WITCH);
        PARTICLES.put("dripWater", Particle.DRIP_WATER);
        PARTICLES.put("dripLava", Particle.DRIP_LAVA);
        PARTICLES.put("angryVillager", Particle.VILLAGER_ANGRY);
        PARTICLES.put("happyVillager", Particle.VILLAGER_HAPPY);
        PARTICLES.put("townaura", Particle.TOWN_AURA);
        PARTICLES.put("note", Particle.NOTE);
        PARTICLES.put("portal", Particle.PORTAL);
        PARTICLES.put("enchantmenttable", Particle.ENCHANTMENT_TABLE);
        PARTICLES.put("flame", Particle.FLAME);
        PARTICLES.put("lava", Particle.LAVA);
        PARTICLES.put("footstep", Particle.FOOTSTEP);
        PARTICLES.put("cloud", Particle.CLOUD);
        PARTICLES.put("reddust", Particle.REDSTONE);
        PARTICLES.put("snowballpoof", Particle.SNOWBALL);
        PARTICLES.put("snowshovel", Particle.SNOW_SHOVEL);
        PARTICLES.put("slime", Particle.SLIME);
        PARTICLES.put("heart", Particle.HEART);
        PARTICLES.put("barrier", Particle.BARRIER);
    }

    /**
     * Creates the instance for this command.
     */
    public ParticleCommand() {
        super("particle");
        setPermission("minecraft.command.particle"); // NON-NLS
    }

    /**
     * Looks up a particle by its vanilla name.
     *
     * @param name the name, in any case
     * @return the particle, or null if the name is unknown or needs extra data
     */
    static Particle byName(String name) {
        return PARTICLES.get(name);
    }

    /**
     * Parses a coordinate that is absolute, or relative with a tilde. Unlike block commands, this
     * does not move whole numbers to the middle of the block.
     */
    private static double coordinate(double origin, String value) {
        if (value.startsWith("~")) {
            return origin + (value.length() > 1 ? Double.parseDouble(value.substring(1)) : 0);
        }
        return Double.parseDouble(value);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args,
            CommandMessages messages) {
        if (!testPermission(sender, messages.getPermissionMessage())) {
            return true;
        }
        if (args.length < 8 || args.length > 11) {
            sendUsageMessage(sender, messages);
            return false;
        }
        Particle particle = byName(args[0]);
        if (particle == null) {
            new LocalizedStringImpl("particle.invalid", messages.getResourceBundle())
                    .sendInColor(ChatColor.RED, sender, args[0]);
            return false;
        }

        double speed;
        double offsetX;
        double offsetY;
        double offsetZ;
        int count = 0;
        Location location;
        try {
            Location origin = CommandUtils.getLocation(sender);
            location = new Location(origin.getWorld(), coordinate(origin.getX(), args[1]),
                    coordinate(origin.getY(), args[2]), coordinate(origin.getZ(), args[3]));
            offsetX = Double.parseDouble(args[4]);
            offsetY = Double.parseDouble(args[5]);
            offsetZ = Double.parseDouble(args[6]);
            speed = Double.parseDouble(args[7]);
            if (args.length > 8) {
                count = Integer.parseInt(args[8]);
            }
        } catch (NumberFormatException e) {
            sendUsageMessage(sender, messages);
            return false;
        }
        if (speed < 0 || count < 0) {
            sendUsageMessage(sender, messages);
            return false;
        }

        if (args.length > 10) {
            Player target = Bukkit.getPlayerExact(args[10]);
            if (target == null) {
                messages.getGeneric(GenericMessage.NO_SUCH_PLAYER)
                        .sendInColor(ChatColor.RED, sender, args[10]);
                return false;
            }
            target.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed);
        } else {
            World world = location.getWorld();
            world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed);
        }
        new LocalizedStringImpl("particle.done", messages.getResourceBundle())
                .send(sender, particle.name().toLowerCase(Locale.ROOT));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args)
            throws IllegalArgumentException {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], PARTICLES.keySet(), new ArrayList<>());
        }
        return Collections.emptyList();
    }
}
