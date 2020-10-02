package science.atlarge.opencraft.opencraft.chunk;

import java.util.Iterator;
import science.atlarge.opencraft.opencraft.GlowWorld;

/**
 * The chunk iterator allows for traversal of an area of interest, either directly or via a for-loop.
 */
public class ChunkIterator implements Iterator<GlowChunk> {

    private final AreaOfInterest area;
    private int x;
    private int z;

    /**
     * Create a chunk iterator over the given area.
     *
     * @param area the area of interest.
     */
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
