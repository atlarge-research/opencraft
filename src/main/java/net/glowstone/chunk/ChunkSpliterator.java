package net.glowstone.chunk;

import java.util.Spliterator;
import java.util.function.Consumer;
import net.glowstone.GlowWorld;

public final class ChunkSpliterator implements Spliterator<GlowChunk> {

    private final AreaOfInterest area;
    private int x;
    private int z;
    private int size;

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