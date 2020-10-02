package science.atlarge.opencraft.opencraft.entity;

import java.util.function.Function;
import org.bukkit.Location;

public abstract class GlowCreatureTest<T extends GlowCreature> extends GlowLivingEntityTest<T> {
    protected GlowCreatureTest(
            Function<Location, ? extends T> entityCreator) {
        super(entityCreator);
    }
}
