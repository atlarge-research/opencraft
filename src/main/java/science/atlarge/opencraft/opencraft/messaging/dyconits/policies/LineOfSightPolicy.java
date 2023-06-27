package science.atlarge.opencraft.opencraft.messaging.dyconits.policies;

import com.flowpowered.network.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import science.atlarge.opencraft.dyconits.Bounds;
import science.atlarge.opencraft.dyconits.Subscriber;
import science.atlarge.opencraft.dyconits.policies.DyconitCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitPolicy;
import science.atlarge.opencraft.dyconits.policies.DyconitSubscribeCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitUnsubscribeCommand;

public class LineOfSightPolicy implements DyconitPolicy<Player, Message> {

    private static final String CATCH_ALL_DYCONIT_NAME = "catch-all";

    private final int viewDistance;
    private final Map<Player, Location> referenceLocation = new HashMap<>();
    private final Map<Player, Set<String>> prevSubscriptions = new HashMap<>();

    public LineOfSightPolicy(int viewDistance) {
        this.viewDistance = viewDistance;
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

    private String chunkToName(Chunk chunk) {
        return chunk.getWorld().getName() + "-" + chunk.getX() + "-" + chunk.getZ();
    }

    @Override
    public @NotNull List<DyconitCommand<Player, Message>> update(Subscriber<Player, Message> sub) {

        // player data vars
        Player player = sub.getKey();
        Location location = player.getLocation();
        int playerViewDistance = player.getViewDistance() << 4;
        java.util.function.Consumer<Message> callback = sub.getCallback();

        // chunks game state updates vars
        List<DyconitCommand<Player, Message>> chunks = new ArrayList<>();

        // policy chunks of interest vars
        List<Block> blocksVisibleList = player.getLineOfSight(null, playerViewDistance);
        Set<Chunk> chunksVisibleSet = new HashSet<>();

        // player subscription vars
        Set<String> playerSubscriptions = new HashSet<>();
        Set<String> prevPlayerSubscriptions = prevSubscriptions.computeIfAbsent(player, p -> new HashSet<>());

        if (referenceLocation.containsKey(player) && referenceLocation.get(player).distanceSquared(location) < 256) {
            return chunks;
        }
        referenceLocation.put(player, location);

        World world = location.getWorld();
        int centerX = location.getChunk().getX();
        int centerZ = location.getChunk().getZ();

        for (Block visibleBlock : blocksVisibleList) {
            chunksVisibleSet.add(visibleBlock.getChunk());
        }

        chunks.add(new DyconitSubscribeCommand<>(player, callback, new Bounds(1000, -1), CATCH_ALL_DYCONIT_NAME));

        for (Chunk visibleChunk : chunksVisibleSet) {
            String dyconitName = chunkToName(visibleChunk);
            int x = visibleChunk.getX();
            int z = visibleChunk.getZ();
            double d = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2));

            chunks.add(new DyconitSubscribeCommand<>(player, callback, new Bounds((int)Math.round(d) * (int)Math.round(d), -1), dyconitName));
            playerSubscriptions.add(dyconitName);
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
        return 1;
    }
}