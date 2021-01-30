package science.atlarge.opencraft.opencraft.io.entity;

import science.atlarge.opencraft.opencraft.entity.monster.GlowEvoker;
import org.bukkit.entity.EntityType;

public class EvokerStore extends MonsterStore<GlowEvoker> {

    public EvokerStore() {
        super(GlowEvoker.class, EntityType.EVOKER, GlowEvoker::new);
    }
}
