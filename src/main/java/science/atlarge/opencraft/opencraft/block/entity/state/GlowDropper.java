package science.atlarge.opencraft.opencraft.block.entity.state;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.dispenser.DefaultDispenseBehavior;
import science.atlarge.opencraft.opencraft.dispenser.DispenseBehavior;
import org.bukkit.Material;
import org.bukkit.block.Dropper;

public class GlowDropper extends GlowDispenser implements Dropper {

    public GlowDropper(GlowBlock block) {
        super(block);
    }

    @Override
    public void drop() {
        dispense();
    }

    @Override
    protected DispenseBehavior getDispenseBehavior(Material itemType) {
        return DefaultDispenseBehavior.INSTANCE;
    }
}
