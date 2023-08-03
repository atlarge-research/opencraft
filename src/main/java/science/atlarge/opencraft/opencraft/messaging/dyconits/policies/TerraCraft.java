package science.atlarge.opencraft.opencraft.messaging.dyconits.policies;

import com.flowpowered.network.Message;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import science.atlarge.opencraft.opencraft.EventFactory;
import science.atlarge.opencraft.opencraft.messaging.dyconits.policies.weights.DistanceMoved;
import science.atlarge.opencraft.opencraft.messaging.dyconits.policies.weights.WeighMessage;
import science.atlarge.opencraft.dyconits.Bounds;
import science.atlarge.opencraft.dyconits.Subscriber;
import science.atlarge.opencraft.dyconits.policies.DyconitCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitPolicy;
import science.atlarge.opencraft.dyconits.policies.DyconitSubscribeCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitUnsubscribeCommand;

public class TerraCraft implements DyconitPolicy<Player, Message> {

    private static final String CATCH_ALL_DYCONIT_NAME = "TERRA";

    private final int viewDistance;
    private final Map<Player, Set<String>> prevSubscriptions = new HashMap<>();
    private static final int INTEREST_SET_SIZE = 5;

    /**
     * 1/3.4 seconds. This value is from a user experiment in the Donnybrook paper.
     * Here they find that user's interest sets changes 3.4 times per second.
     */
    private final Duration turnoverTime = Duration.ofNanos(294117647);

    private final Map<Subscriber<Player, Message>, Instant> lastChangedMap = new HashMap<>();
    private final Map<Subscriber<Player, Message>, List<Player>> interestSet = new HashMap<>();
    private final Bounds oneSecondBound = new Bounds(1000, -1);
    private final Random random = new Random(System.currentTimeMillis());
    private final WeighMessage weighMessage;

    public TerraCraft(int viewDistance, Server server) {
        this.viewDistance = viewDistance;
        this.weighMessage = new DistanceMoved(server, new Bounds(-1, 5));
    }

    @NotNull
    @Override
    public String computeAffectedDyconit(@NotNull Object publisher) {
        if (publisher instanceof Chunk) {
            Chunk chunk = (Chunk) publisher;
            return chunkToName(chunk);
        }

        if (publisher instanceof Block) {
            Chunk chunk = ((Block) publisher).getChunk();
            return chunkToName(chunk);
        }

        if (publisher instanceof Player) {
            return entityToName((Player) publisher);
        }

        if (publisher instanceof Entity) {
            Chunk chunk = ((Entity) publisher).getChunk();
            return chunkToName(chunk);
        }

        if (publisher instanceof Location) {
            Chunk chunk = ((Location) publisher).getChunk();
            return chunkToName(chunk);
        }

        return CATCH_ALL_DYCONIT_NAME;
    }

    private String entityToName(Player player) {
        return player.getName();
    }

    private String chunkToName(Chunk chunk) {
        return chunk.getWorld().getName() + "-" + chunk.getX() + "-" + chunk.getZ();
    }

    @Override
    public @NotNull List<DyconitCommand<Player, Message>> update(Subscriber<Player, Message> sub) {

        // player data vars
        Player player = sub.getKey();
        Location location = player.getLocation();
        World world = player.getWorld();
        Biome biome = world.getBiome(location.getBlockX(), location.getBlockZ());
        int playerViewDistance = player.getViewDistance() << 4;
        java.util.function.Consumer<Message> callback = sub.getCallback();

        // chunks game state updates vars
        List<DyconitCommand<Player, Message>> chunks = new ArrayList<>();

        // policy chunks of interest vars
        int radius = Math.min(this.viewDistance, playerViewDistance);
        List<Block> blocksVisibleList = player.getLineOfSight(null, radius);
        Set<Chunk> chunksVisibleSet = new HashSet<>();

        // player subscription vars
        Set<String> playerSubscriptions = new HashSet<>();
        Set<String> prevPlayerSubscriptions = prevSubscriptions.computeIfAbsent(player, p -> new HashSet<>());

        int centerX = location.getChunk().getX();
        int centerZ = location.getChunk().getZ();

        for (Block visibleBlock : blocksVisibleList) {
            chunksVisibleSet.add(visibleBlock.getChunk());
        }

        if (location.getChunk().getEntities().length >= 25) {
                // Make sure not all players have the same lastChangedTime, to
            Instant lastChanged = lastChangedMap.computeIfAbsent(sub, p -> Instant.ofEpochMilli(System.currentTimeMillis() - ThreadLocalRandom.current().nextLong(turnoverTime.toMillis())));
            List<DyconitCommand<Player, Message>> commands = new ArrayList<>();
            Player thisPlayer = sub.getKey();
            commands.add(new DyconitSubscribeCommand<>(thisPlayer, callback, Bounds.Companion.getZERO(), CATCH_ALL_DYCONIT_NAME));
            // We update players' interest sets after turnoverTime has passed. See documentation at variable declaration.
            if (!interestSet.containsKey(sub)) {
                interestSet.put(sub, new ArrayList<>());
                thisPlayer.getWorld().getPlayers().stream()
                        .filter(p -> !p.equals(thisPlayer))
                        .map(p -> new DyconitSubscribeCommand<>(thisPlayer, callback, oneSecondBound, entityToName(p)))
                        .forEach(commands::add);
            } else if (turnoverTime.minus(Duration.between(lastChanged, Instant.now())).isNegative()) {
                Set<Player> allPlayers = new HashSet<>(thisPlayer.getWorld().getPlayers());
                allPlayers.remove(thisPlayer);
                if (allPlayers.size() > 0) {
                    List<Player> playerSubscriberSet = interestSet.computeIfAbsent(sub, s -> new ArrayList<>());
                    List<Player> candidates = allPlayers.stream().filter(p -> !playerSubscriberSet.contains(p)).collect(Collectors.toList());
                    // We randomly select which players to add and remove from the interest set.
                    // This is different from Donnybrook's approach, which calculates an attention score.
                    // Because we think the difference in bandwidth usage is negligible, we use our own, simpler, approach.
                    if (candidates.size() > 0) {
                        if (playerSubscriberSet.size() >= INTEREST_SET_SIZE) {
                            Player removed = playerSubscriberSet.remove(random.nextInt(playerSubscriberSet.size()));
                            commands.add(new DyconitSubscribeCommand<>(thisPlayer, callback, oneSecondBound, entityToName(removed)));
                        }

                        Player added = candidates.get(random.nextInt(candidates.size()));
                        commands.add(new DyconitSubscribeCommand<>(thisPlayer, callback, Bounds.Companion.getZERO(), entityToName(added)));
                        playerSubscriberSet.add(added);
                    }
                    lastChangedMap.put(sub, Instant.now());
                }
            }
            return commands;
        }
        else if (biome.toString().equals("PLAINS") || biome.toString().equals("SNOWY_PLAINS") || biome.toString().equals("SUNFLOWER_PLAINS") || EventFactory.getNumPlayers().intValue() < 10) {
            chunks.add(new DyconitSubscribeCommand<>(sub.getKey(), sub.getCallback(), Bounds.Companion.getZERO(), CATCH_ALL_DYCONIT_NAME));

            for (int x = centerX - radius; x <= centerX + radius; x++) {
                for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                    Chunk chunk = world.getChunkAt(x, z);
                    String dyconitName = chunkToName(chunk);
                    int distance = Math.abs(centerX - x) + Math.abs(centerZ - z);
    
                    if (distance > 0) {
                        chunks.add(new DyconitSubscribeCommand<>(sub.getKey(), sub.getCallback(), new Bounds(1000, distance * distance), dyconitName));
                    }
                    else {
                        chunks.add(new DyconitSubscribeCommand<>(sub.getKey(), sub.getCallback(), Bounds.Companion.getZERO(), dyconitName));
                    }
                    
                    playerSubscriptions.add(dyconitName);
                }
            }
        }
        else {
            chunks.add(new DyconitSubscribeCommand<>(player, callback, new Bounds(2000, -1), CATCH_ALL_DYCONIT_NAME));

            for (Chunk visibleChunk : chunksVisibleSet) {
                String dyconitName = chunkToName(visibleChunk);
                int x = visibleChunk.getX();
                int z = visibleChunk.getZ();
                int distance = (int)Math.round(Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2)));

                chunks.add(new DyconitSubscribeCommand<>(player, callback, new Bounds(50 * distance * distance, -1), dyconitName));
                playerSubscriptions.add(dyconitName);
            }
        }

        for (String dyconitName : prevPlayerSubscriptions) {
            if (!playerSubscriptions.contains(dyconitName)) {
                chunks.add(new DyconitUnsubscribeCommand<>(player, dyconitName));
            }
        }
        prevSubscriptions.put(player, playerSubscriptions);

        

        return chunks;
    }

    @Override
    public int weigh(Message message) {
        return weighMessage.weigh(message);
    }
}
