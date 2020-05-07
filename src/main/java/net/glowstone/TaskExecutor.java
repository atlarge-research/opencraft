package net.glowstone;

import net.glowstone.net.GlowSession;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutor {

    private static Executor executor = Executors.newCachedThreadPool();

    /**
     * Execute the provided task for a specific session.
     *
     * @param session The session for a player.
     * @param task The task to be run in a session.
     */
    public static void execute(GlowSession session, Runnable task) {
        executor.execute(() -> {
            task.run();
            Runnable next = session.nextTask();
            if(next != null) {
                execute(session, next);
            }
        });
    }

}
