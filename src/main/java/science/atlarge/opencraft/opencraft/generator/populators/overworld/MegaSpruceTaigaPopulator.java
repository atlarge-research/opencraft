package science.atlarge.opencraft.opencraft.generator.populators.overworld;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.TreeDecorator.TreeDecoration;
import science.atlarge.opencraft.opencraft.generator.objects.trees.MegaSpruceTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.RedwoodTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.TallRedwoodTree;
import org.bukkit.block.Biome;

public class MegaSpruceTaigaPopulator extends MegaTaigaPopulator {

    private static final Biome[] BIOMES = {Biome.MUTATED_REDWOOD_TAIGA,
        Biome.MUTATED_REDWOOD_TAIGA_HILLS};
    private static final TreeDecoration[] TREES = {new TreeDecoration(RedwoodTree::new, 44),
        new TreeDecoration(TallRedwoodTree::new, 22),
        new TreeDecoration(MegaSpruceTree::new, 33)};

    public MegaSpruceTaigaPopulator() {
        treeDecorator.setTrees(TREES);
    }

    @Override
    public Collection<Biome> getBiomes() {
        return Collections.unmodifiableList(Arrays.asList(BIOMES));
    }
}
