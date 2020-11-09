package net.glowstone.chunk.policy;

import com.flowpowered.network.Message;
import com.flowpowered.network.session.Session;
import com.google.common.collect.ImmutableMap;
import net.glowstone.GlowWorld;
import net.glowstone.chunk.AreaOfInterest;
import net.glowstone.chunk.ChunkManager;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.executor.ChunkRunnable;
import net.glowstone.executor.PriorityExecutor;
import net.glowstone.net.message.play.game.UnloadChunkMessage;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import science.atlarge.opencraft.messaging.MessagingSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ChunkLoadingPolicy {

    GlowWorld world;

    ImmutableMap<GlowPlayer, AreaOfInterest> previousAreas;

    final PriorityExecutor<ChunkRunnable> executor;

    public ChunkLoadingPolicy(GlowWorld world) {
        this.world = world;
        previousAreas = ImmutableMap.of();
        executor = new PriorityExecutor<>();
    }

    public static ChunkLoadingPolicy fromInt(GlowWorld world, int index) {
        if (index == 0) {
            return new NaiveServerlessChunkLoadingPolicy(world);
        }
        return new DefaultChunkLoadingPolicy(world);
    }

    public abstract int getPolicyIndex();

    /**
     * Called every tick
     *
     * @param players player list
     */
    public abstract void update(Collection<GlowPlayer> players, MessagingSystem<Chunk, Object, Player, Message> messagingSystem);

    /**
     * Called on world shutdown
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * Find the chunks that are in the first area of interest, but not in the second.
     *
     * @param first the first area of interest.
     * @param second the second area of interest.
     * @return the list of chunks.
     */
    List<ChunkRunnable> getDifference(GlowPlayer player, AreaOfInterest first, AreaOfInterest second) {

        if (first == null || first.equals(second)) {
            return new ArrayList<>();
        }

        if (second == null || first.getWorld() != second.getWorld()) {
            List<ChunkRunnable> runnables = new ArrayList<>();
            first.forEach(chunk -> {
                ChunkRunnable runnable = new ChunkRunnable(player, chunk);
                runnables.add(runnable);
            });
            return runnables;
        }

        List<ChunkRunnable> runnables = new ArrayList<>();
        first.forEach(chunk -> {
            if (!second.contains(chunk)) {
                ChunkRunnable runnable = new ChunkRunnable(player, chunk);
                runnables.add(runnable);
            }
        });
        return runnables;
    }

    /**
     * Unload all the given chunks, except for the ones that have been cancelled. Because, the ones that were cancelled
     * were never streamed to the player.
     *
     * @param toUnload The chunks to be unloaded.
     */
    void unloadChunks(List<ChunkRunnable> toUnload) {
        toUnload.forEach(runnable -> {

            GlowPlayer player = runnable.getPlayer();
            GlowChunk chunk = runnable.getChunk();

            Message message = new UnloadChunkMessage(chunk.getX(), chunk.getZ());
            Session session = player.getSession();
            session.send(message);

            GlowChunk.Key key = GlowChunk.Key.of(chunk.getX(), chunk.getZ());
            ChunkManager.ChunkLock lock = player.getChunkLock();
            lock.release(key);
        });
    }
}
