package science.atlarge.opencraft.opencraft.chunk.policy;

import com.flowpowered.network.session.Session;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.chunk.AreaOfInterest;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.executor.ChunkRunnable;
import science.atlarge.opencraft.opencraft.messaging.Messaging;

public class DefaultChunkLoadingPolicy extends ChunkLoadingPolicy {

    public DefaultChunkLoadingPolicy(GlowWorld world) {
        super(world);
    }

    @Override
    public void update(Collection<GlowPlayer> players, Messaging messagingSystem) {
        ImmutableMap.Builder<GlowPlayer, AreaOfInterest> currentAreasBuilder = ImmutableMap.builder();
        players.forEach(player -> currentAreasBuilder.put(player, player.getAreaOfInterest()));
        ImmutableMap<GlowPlayer, AreaOfInterest> currentAreas = currentAreasBuilder.build();

        Set<GlowPlayer> currentPlayers = currentAreas.keySet();
        Set<GlowPlayer> previousPlayers = previousAreas.keySet();
        Sets.SetView<GlowPlayer> allPlayers = Sets.union(currentPlayers, previousPlayers);

        allPlayers.parallelStream()
                .forEach(player -> {
                    Session session = player.getSession();
                    messagingSystem.update(player, session::send);
                });

        List<ChunkRunnable> chunksToLoad = allPlayers.parallelStream()
                .map(player -> {
                    AreaOfInterest current = currentAreas.get(player);
                    AreaOfInterest previous = previousAreas.get(player);
                    return getChunksToLoad(player, current, previous);
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Set<ChunkRunnable> cancelled = executor.executeAndCancel(chunksToLoad, ChunkRunnable::shouldBeCancelled);

        List<ChunkRunnable> chunksToUnload = allPlayers.parallelStream()
                .map(player -> {
                    AreaOfInterest current = currentAreas.get(player);
                    AreaOfInterest previous = previousAreas.get(player);
                    return getChunksToUnload(player, current, previous);
                })
                .flatMap(List::stream)
                .filter(runnable -> !cancelled.contains(runnable))
                .collect(Collectors.toList());

        unloadChunks(chunksToUnload);

        previousAreas = currentAreas;
    }

    /**
     * List chunks that are in the current area of interest, but not in the previous.
     *
     * @param current  the current area of interest.
     * @param previous the previous area of interest.
     * @return the list of chunks.
     */
    List<ChunkRunnable> getChunksToLoad(GlowPlayer player, AreaOfInterest current, AreaOfInterest previous) {
        return getDifference(player, current, previous);
    }

    /**
     * Find chunks that are in the previous area of interest, but not in the current.
     *
     * @param current  the current area of interest.
     * @param previous the previous area of interest.
     * @return the list of chunks.
     */
    List<ChunkRunnable> getChunksToUnload(GlowPlayer player, AreaOfInterest current, AreaOfInterest previous) {
        return getDifference(player, previous, current);
    }
}
