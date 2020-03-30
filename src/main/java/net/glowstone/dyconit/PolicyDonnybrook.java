package net.glowstone.dyconit;

import java.util.HashSet;
import java.util.Set;
import net.glowstone.chunk.GlowChunk;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

/*
Policy 1: DONNYBROOK
 */

class PolicyDonnybrook {
    private static final PolicyDonnybrook policy = new PolicyDonnybrook();

    private PolicyDonnybrook() {}

    static PolicyDonnybrook setPolicy() {
        return policy;
    }

    void enforce(Player p) {
        DyconitCollection dyconits = DyconitManager.getDyconits();
        Set nearbyKeys = getNearbyChunkKeys(p, 1);

        for (GlowChunk.Key key : dyconits.getKeyDyconitMap().keySet()) {
            Dyconit.Subscription sub = dyconits.retrieveDyconit(key)
                                                        .subscriptions.get(p);

            if (nearbyKeys.contains(key)) {
                sub.stalenessBound = 100;
            } else {
                sub.stalenessBound = 5000;
            }
        }
    }

    private Set getNearbyChunkKeys(Player player, int distance) {
        Set<GlowChunk.Key> chunkKeys = new HashSet<>();

        Chunk currentChunk = player.getChunk();
        int xloc = currentChunk.getX();
        int zloc = currentChunk.getZ();

        for (int i = -distance; i <= distance; i++) {
            for (int j = -distance; j <= distance; j++) {
                chunkKeys.add(GlowChunk.Key.of(xloc += i, zloc += j));
            }
        }
        return chunkKeys;
    }
}
