package net.glowstone.executor;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class DynamicExecutor {

    private final ThreadPoolExecutor executor;

    public DynamicExecutor() {
        executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new PriorityBlockingQueue<>());
    }

    public void clearQueue() {
        executor.getQueue().clear();
    }

    public void execute(@NotNull Runnable command, double priority) {
        PriorityRunnable priorityRunnable = new PriorityRunnable(command, priority);
        executor.execute(priorityRunnable);
    }
}
