package net.glowstone.dyconit;

import java.util.Set;
import net.glowstone.chunk.GlowChunk;
import org.bukkit.entity.Player;

/*
Policy 1: DONNYBROOK
 */

class PolicyDonnybrook implements IPolicy{
    private static final PolicyDonnybrook policy = new PolicyDonnybrook();

    private PolicyDonnybrook() {}

    static IPolicy setPolicy() {
        return policy;
    }

    @Override
    public void enforce(Player p, DyconitCollection dyconits) {
        Set nearbyKeys = DyconitManager.getNearbyChunkKeys(p, 1);

        for (GlowChunk.Key key : dyconits.getKeyDyconitMap().keySet()) {
            Dyconit.Subscription sub = dyconits.retrieveDyconit(key)
                                                        .subscriptions.get(p);

            if (sub == null) { continue; }

            if (nearbyKeys.contains(key)) {
                sub.stalenessBound = 100;
            }  else {
                sub.stalenessBound = 500;
            }
        }
    }
}
