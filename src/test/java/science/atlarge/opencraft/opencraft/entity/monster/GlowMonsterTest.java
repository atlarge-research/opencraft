package science.atlarge.opencraft.opencraft.entity.monster;

import java.util.function.Function;
import science.atlarge.opencraft.opencraft.entity.GlowCreatureTest;
import org.bukkit.Location;

public abstract class GlowMonsterTest<T extends GlowMonster> extends GlowCreatureTest<T> {

    protected GlowMonsterTest(
            Function<Location, ? extends T> entityCreator) {
        super(entityCreator);
    }
}
