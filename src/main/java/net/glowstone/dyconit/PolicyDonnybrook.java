package net.glowstone.dyconit;

import java.util.Set;
import net.glowstone.chunk.GlowChunk;
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
        Set nearbyKeys = DyconitManager.getNearbyChunkKeys(p, 1);
        Set farKeys = DyconitManager.getNearbyChunkKeys(p, 2);
        farKeys.removeAll(nearbyKeys);

        for (GlowChunk.Key key : dyconits.getKeyDyconitMap().keySet()) {
            Dyconit.Subscription sub = dyconits.retrieveDyconit(key)
                                                        .subscriptions.get(p);

            if (sub == null) {
                continue;
            }

            if (nearbyKeys.contains(key)) {
                sub.stalenessBound = 100;
            } else if (farKeys.contains(key)) {
                sub.stalenessBound = 200;
            } else {
                sub.stalenessBound = 1000;
            }
        }
    }
}
