package science.atlarge.opencraft.opencraft.entity;

import java.util.function.Function;
import org.bukkit.Location;

public abstract class GlowHangingEntityTest<T extends GlowHangingEntity> extends GlowEntityTest<T> {
    protected GlowHangingEntityTest(
            Function<Location, ? extends T> entityCreator) {
        super(entityCreator);
    }
}
