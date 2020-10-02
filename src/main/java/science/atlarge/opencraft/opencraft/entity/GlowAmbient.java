package science.atlarge.opencraft.opencraft.entity;

import org.bukkit.Location;
import org.bukkit.entity.Ambient;

public abstract class GlowAmbient extends GlowLivingEntity implements Ambient {

    public GlowAmbient(Location location, double maxHealth) {
        super(location, maxHealth);
    }
}
