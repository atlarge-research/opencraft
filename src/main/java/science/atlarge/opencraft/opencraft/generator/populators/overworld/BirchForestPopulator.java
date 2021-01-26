package science.atlarge.opencraft.opencraft.generator.populators.overworld;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.TreeDecorator.TreeDecoration;
import science.atlarge.opencraft.opencraft.generator.objects.trees.BirchTree;
import org.bukkit.block.Biome;

public class BirchForestPopulator extends ForestPopulator {

    private static final Biome[] BIOMES = {Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_HILLS};
    private static final TreeDecoration[] TREES = {new TreeDecoration(BirchTree::new, 1)};

    public BirchForestPopulator() {
        treeDecorator.setTrees(TREES);
    }

    @Override
    public Collection<Biome> getBiomes() {
        return Collections.unmodifiableList(Arrays.asList(BIOMES));
    }
}
