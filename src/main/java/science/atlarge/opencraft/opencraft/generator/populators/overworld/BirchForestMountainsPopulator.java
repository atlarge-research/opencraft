package science.atlarge.opencraft.opencraft.generator.populators.overworld;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.TreeDecorator.TreeDecoration;
import science.atlarge.opencraft.opencraft.generator.objects.trees.BirchTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.TallBirchTree;
import org.bukkit.block.Biome;

public class BirchForestMountainsPopulator extends BirchForestPopulator {

    private static final Biome[] BIOMES = {Biome.MUTATED_BIRCH_FOREST,
        Biome.MUTATED_BIRCH_FOREST_HILLS};
    private static final TreeDecoration[] TREES = {new TreeDecoration(BirchTree::new, 1),
        new TreeDecoration(TallBirchTree::new, 1)};

    public BirchForestMountainsPopulator() {
        treeDecorator.setTrees(TREES);
    }

    @Override
    public Collection<Biome> getBiomes() {
        return Collections.unmodifiableList(Arrays.asList(BIOMES));
    }
}
