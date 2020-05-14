package net.glowstone.executor;

import org.jetbrains.annotations.NotNull;

public class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {

    private final Runnable runnable;
    private final double priority;

    public PriorityRunnable(Runnable runnable, double priority) {
        this.runnable = runnable;
        this.priority = priority;
    }

    @Override
    public void run() {
        runnable.run();
    }

    @Override
    public int compareTo(@NotNull PriorityRunnable other) {
        return Double.compare(priority, other.priority);
    }
}
