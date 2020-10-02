package science.atlarge.opencraft.opencraft.generator.populators.overworld;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.MelonDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.TreeDecorator.TreeDecoration;
import science.atlarge.opencraft.opencraft.generator.objects.trees.BigOakTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.CocoaTree;
import science.atlarge.opencraft.opencraft.generator.objects.trees.JungleBush;
import science.atlarge.opencraft.opencraft.generator.objects.trees.MegaJungleTree;
import science.atlarge.opencraft.opencraft.util.BlockStateDelegate;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

public class JunglePopulator extends BiomePopulator {

    private static final Biome[] BIOMES = {Biome.JUNGLE, Biome.JUNGLE_HILLS, Biome.MUTATED_JUNGLE};
    private static final TreeDecoration[] TREES = {new TreeDecoration(BigOakTree::new, 10),
        new TreeDecoration(JungleBush::new, 50), new TreeDecoration(MegaJungleTree::new, 15),
        new TreeDecoration(CocoaTree::new, 30)};

    private final MelonDecorator melonDecorator = new MelonDecorator();

    /**
     * Creates a populator specialized for jungles.
     */
    public JunglePopulator() {
        treeDecorator.setAmount(65);
        treeDecorator.setTrees(TREES);
        flowerDecorator.setAmount(4);
        tallGrassDecorator.setAmount(25);
        tallGrassDecorator.setFernDensity(0.25D);
    }

    @Override
    public Collection<Biome> getBiomes() {
        return Collections.unmodifiableList(Arrays.asList(BIOMES));
    }

    @Override
    public void populateOnGround(World world, Random random, Chunk chunk) {

        int sourceX = chunk.getX() << 4;
        int sourceZ = chunk.getZ() << 4;

        for (int i = 0; i < 7; i++) {
            int x = sourceX + random.nextInt(16);
            int z = sourceZ + random.nextInt(16);
            int y = world.getHighestBlockYAt(x, z);
            Block sourceBlock = world.getBlockAt(x, y, z);
            BlockStateDelegate delegate = new BlockStateDelegate();
            JungleBush bush = new JungleBush(random, delegate);
            if (bush.generate(sourceBlock.getLocation())) {
                delegate.updateBlockStates();
            }
        }

        super.populateOnGround(world, random, chunk);
        melonDecorator.populate(world, random, chunk);
    }
}
