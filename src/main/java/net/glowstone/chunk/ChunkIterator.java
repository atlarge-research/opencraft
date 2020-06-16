package net.glowstone.chunk;

import java.util.Iterator;
import net.glowstone.GlowWorld;

public class ChunkIterator implements Iterator<GlowChunk> {

    private final AreaOfInterest area;
    private int x;
    private int z;

    ChunkIterator(AreaOfInterest area) {
        this.area = area;
        x = area.getCenterX() - area.getRadius();
        z = area.getCenterZ() - area.getRadius();
    }

    @Override
    public boolean hasNext() {
        return z <= area.getCenterZ() + area.getRadius();
    }

    @Override
    public GlowChunk next() {
        GlowWorld world = area.getWorld();
        GlowChunk chunk = world.getChunkAt(x, z);
        x++;
        if (x > area.getCenterX() + area.getRadius()) {
            x = area.getCenterZ() - area.getRadius();
            z++;
        }
        return chunk;
    }
}
