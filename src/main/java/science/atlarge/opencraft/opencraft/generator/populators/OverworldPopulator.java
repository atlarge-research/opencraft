package science.atlarge.opencraft.opencraft.generator.populators;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.BiomePopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.BirchForestMountainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.BirchForestPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.DesertMountainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.DesertPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.ExtremeHillsPlusPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.ExtremeHillsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.FlowerForestPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.ForestPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.IcePlainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.IcePlainsSpikesPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.JungleEdgePopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.JunglePopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.MegaSpruceTaigaPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.MegaTaigaPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.MesaForestPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.MesaPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.MushroomIslandPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.OceanPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.PlainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.RoofedForestPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.SavannaMountainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.SavannaPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.SunflowerPlainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.SwamplandPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.TaigaPopulator;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

public class OverworldPopulator extends BlockPopulator {

    private final Map<Biome, BiomePopulator> biomePopulators = new EnumMap<>(Biome.class);

    /**
     * Creates a populator with biome populators for all vanilla overworld biomes.
     */
    public OverworldPopulator() {
        registerBiomePopulator(new BiomePopulator()); // defaults applied to all biomes
        registerBiomePopulator(new PlainsPopulator());
        registerBiomePopulator(new SunflowerPlainsPopulator());
        registerBiomePopulator(new ForestPopulator());
        registerBiomePopulator(new BirchForestPopulator());
        registerBiomePopulator(new BirchForestMountainsPopulator());
        registerBiomePopulator(new RoofedForestPopulator());
        registerBiomePopulator(new FlowerForestPopulator());
        registerBiomePopulator(new DesertPopulator());
        registerBiomePopulator(new DesertMountainsPopulator());
        registerBiomePopulator(new JunglePopulator());
        registerBiomePopulator(new JungleEdgePopulator());
        registerBiomePopulator(new SwamplandPopulator());
        registerBiomePopulator(new TaigaPopulator());
        registerBiomePopulator(new MegaTaigaPopulator());
        registerBiomePopulator(new MegaSpruceTaigaPopulator());
        registerBiomePopulator(new IcePlainsPopulator());
        registerBiomePopulator(new IcePlainsSpikesPopulator());
        registerBiomePopulator(new SavannaPopulator());
        registerBiomePopulator(new SavannaMountainsPopulator());
        registerBiomePopulator(new ExtremeHillsPopulator());
        registerBiomePopulator(new ExtremeHillsPlusPopulator());
        registerBiomePopulator(new MesaPopulator());
        registerBiomePopulator(new MesaForestPopulator());
        registerBiomePopulator(new MushroomIslandPopulator());
        registerBiomePopulator(new OceanPopulator());
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        Biome biome = world.getBiome((chunk.getX() << 4) + 8, (chunk.getZ() << 4) + 8);
        if (biomePopulators.containsKey(biome)) {
            biomePopulators.get(biome).populate(world, random, chunk);
        }
    }

    private void registerBiomePopulator(BiomePopulator populator) {
        for (Biome biome : populator.getBiomes()) {
            biomePopulators.put(biome, populator);
        }
    }
}
