package net.glowstone.executor;

import java.util.Comparator;

public class ChunkRunnableComparator implements Comparator<ChunkRunnable> {

    @Override
    public int compare(ChunkRunnable runnable1, ChunkRunnable runnable2) {
        return Double.compare(runnable1.getPriority(), runnable2.getPriority());
    }
}
