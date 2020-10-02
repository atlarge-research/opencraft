package science.atlarge.opencraft.opencraft.io.structure;

import java.util.Random;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.generator.structures.GlowTemple;

public class TempleStore extends StructureStore<GlowTemple> {

    public TempleStore() {
        super(GlowTemple.class, "Temple");
    }

    @Override
    public GlowTemple createStructure(GlowWorld world, int chunkX, int chunkZ) {
        return new GlowTemple(world, chunkX, chunkZ);
    }

    @Override
    public GlowTemple createNewStructure(GlowWorld world, Random random, int chunkX, int chunkZ) {
        return new GlowTemple(world, random, chunkX, chunkZ);
    }
}
