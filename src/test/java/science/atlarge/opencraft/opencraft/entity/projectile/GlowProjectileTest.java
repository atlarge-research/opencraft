package science.atlarge.opencraft.opencraft.entity.projectile;

import java.util.function.Function;
import science.atlarge.opencraft.opencraft.entity.GlowEntityTest;
import org.bukkit.Location;

public abstract class GlowProjectileTest<T extends GlowProjectile> extends GlowEntityTest<T> {

    protected GlowProjectileTest(
            Function<? super Location, ? extends T> entityCreator) {
        super(entityCreator);
    }
}
