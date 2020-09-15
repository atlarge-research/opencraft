package net.glowstone.messaging.dyconits.policies;

import com.flowpowered.network.Message;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import science.atlarge.opencraft.dyconits.Bounds;
import science.atlarge.opencraft.dyconits.Subscriber;
import science.atlarge.opencraft.dyconits.policies.DyconitCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitPolicy;
import science.atlarge.opencraft.dyconits.policies.DyconitSubscribeCommand;

public class DyconitChunkPolicy implements DyconitPolicy<Player, Message> {

    private final World world;
    private final int viewDistance;
    private final String catchAllDyconitName = "catch-all";

    public DyconitChunkPolicy(World world, int viewDistance) {
        this.world = world;
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

        return catchAllDyconitName;
    }

    private String chunkToName(Chunk chunk) {
        return chunk.getX() + "-" + chunk.getZ();
    }

    @Override
    public List<DyconitCommand<Player, Message>> update(Subscriber<Player, Message> sub) {
        Location location = sub.getKey().getLocation();
        if (location.getWorld() != world) {
            return new ArrayList<>();
        }

        int centerX = location.getBlockX() >> 4;
        int centerZ = location.getBlockZ() >> 4;
        int radius = Math.min(viewDistance, sub.getKey().getViewDistance());

        List<DyconitCommand<Player, Message>> chunks = new ArrayList<>();
        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                Chunk chunk = world.getChunkAt(x, z);
                chunks.add(new DyconitSubscribeCommand<>(sub.getKey(), sub.getCallback(), new Bounds(Integer.MAX_VALUE / 2, 2), chunk.getX() + "-" + chunk.getZ()));
            }
        }
        return chunks;
    }

    @Override
    public int weigh(Message message) {
        return 1;
    }
}
