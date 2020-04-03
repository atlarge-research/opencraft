package net.glowstone.dyconit;

/*
Policy 1: Epicenter
 */

import net.glowstone.chunk.GlowChunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class PolicyEpicenter implements IPolicy {
    private static final PolicyEpicenter policy = new PolicyEpicenter();

    private PolicyEpicenter() {}

    static IPolicy setPolicy() { return policy; }

    @Override
    public void enforce(Player p, DyconitCollection dyconits) {
        int distance = 5;
        int upperboundStaleness = 10;
        Map<GlowChunk.Key, Integer> km = new HashMap<>();

        for (int i = distance; i > 0; i--) {
            int stalenessValue = (int) Math.pow(upperboundStaleness, i);
            Set<GlowChunk.Key> keyRing = DyconitManager.getNearbyChunkKeys(p, i);
            keyRing.forEach(k -> km.put(k, stalenessValue));
        }

        for (GlowChunk.Key key : dyconits.getKeyDyconitMap().keySet()) {
            Dyconit.Subscription sub = dyconits.retrieveDyconit(key)
                                                        .subscriptions.get(p);

            if (sub == null) { continue; }

            sub.stalenessBound = km.getOrDefault(key, 100000);
        }
    }
}
