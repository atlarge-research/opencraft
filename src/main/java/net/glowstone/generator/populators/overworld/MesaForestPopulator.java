package net.glowstone.generator.populators.overworld;

import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MesaForestPopulator extends MesaPopulator {

    private static final Biome[] BIOMES = {Biome.MESA_ROCK, Biome.MUTATED_MESA_ROCK};

    public MesaForestPopulator() {
        super();
        treeDecorator.setAmount(5);
    }

    @Override
    public Collection<Biome> getBiomes() {
        return Collections.unmodifiableList(Arrays.asList(BIOMES));
    }
}
