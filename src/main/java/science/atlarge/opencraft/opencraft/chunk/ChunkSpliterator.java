package science.atlarge.opencraft.opencraft.chunk;

import java.util.Spliterator;
import java.util.function.Consumer;
import science.atlarge.opencraft.opencraft.GlowWorld;

/**
 * The chunk spliterator allows for traversal of an area of interest, either directly or via a stream.
 */
public final class ChunkSpliterator implements Spliterator<GlowChunk> {

    private final AreaOfInterest area;
    private int x;
    private int z;
    private int size;

    /**
     * Create a spliterator over the given area.
     *
     * @param area the area of interest.
     */
    ChunkSpliterator(AreaOfInterest area) {
        this.area = area;
        int radius = area.getRadius();
        x = area.getCenterX() - radius;
        z = area.getCenterZ() - radius;
        int length = (2 * radius + 1);
        size = length * length;
    }

    @Override
    public boolean tryAdvance(Consumer<? super GlowChunk> action) {

        if (z > area.getCenterZ() + area.getRadius()) {
            return false;
        }

        GlowWorld world = area.getWorld();
        GlowChunk chunk = world.getChunkAt(x, z);
        action.accept(chunk);

        x++;
        if (x > area.getCenterX() + area.getRadius()) {
            x = area.getCenterX() - area.getRadius();
            z++;
        }
        size--;

        return true;
    }

    @Override
    public Spliterator<GlowChunk> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return size;
    }

    @Override
    public int characteristics() {
        return ORDERED | DISTINCT | SORTED | SIZED | NONNULL | IMMUTABLE;
    }
}
