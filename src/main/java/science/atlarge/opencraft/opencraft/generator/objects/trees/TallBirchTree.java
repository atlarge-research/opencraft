package science.atlarge.opencraft.opencraft.generator.objects.trees;

import java.util.Random;
import science.atlarge.opencraft.opencraft.util.BlockStateDelegate;

public class TallBirchTree extends BirchTree {

    public TallBirchTree(Random random, BlockStateDelegate delegate) {
        super(random, delegate);
        setHeight(height + random.nextInt(7));
    }
}
