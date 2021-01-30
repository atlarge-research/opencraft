package science.atlarge.opencraft.opencraft.generator.populators.overworld;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.DoublePlantDecorator.DoublePlantDecoration;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.TreeDecorator.TreeDecoration;
import science.atlarge.opencraft.opencraft.generator.objects.trees.AcaciaTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.GenericTree;
import org.bukkit.block.Biome;
import org.bukkit.material.types.DoublePlantSpecies;

public class SavannaPopulator extends BiomePopulator {

    private static final Biome[] BIOMES = {Biome.SAVANNA, Biome.SAVANNA_ROCK};
    private static final DoublePlantDecoration[] DOUBLE_PLANTS = {
        new DoublePlantDecoration(DoublePlantSpecies.DOUBLE_TALLGRASS, 1)};
    private static final TreeDecoration[] TREES = {new TreeDecoration(AcaciaTree::new, 4),
        new TreeDecoration(GenericTree::new, 1)};

    /**
     * Creates a populator specialized for the Savanna and Savanna Plateau biomes.
     */
    public SavannaPopulator() {
        doublePlantDecorator.setAmount(7);
        doublePlantDecorator.setDoublePlants(DOUBLE_PLANTS);
        treeDecorator.setAmount(1);
        treeDecorator.setTrees(TREES);
        flowerDecorator.setAmount(4);
        tallGrassDecorator.setAmount(20);
    }

    @Override
    public Collection<Biome> getBiomes() {
        return Collections.unmodifiableList(Arrays.asList(BIOMES));
    }
}
