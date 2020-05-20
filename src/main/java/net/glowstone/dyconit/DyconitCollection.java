package net.glowstone.dyconit;

import com.google.common.collect.Maps;
import java.util.Map;
import net.glowstone.chunk.GlowChunk;
import org.bukkit.entity.Player;

class DyconitCollection {
    //TODO: FIX CONCURRENCY ISSUE
    private Map<GlowChunk.Key, Dyconit> chunkList;

    DyconitCollection() {
        chunkList = Maps.newConcurrentMap();
    }

    Dyconit retrieveDyconit(GlowChunk.Key key) {
        return chunkList.get(key);
    }

    Map<GlowChunk.Key, Dyconit> getKeyDyconitMap() {
        return chunkList;
    }

    void insertDyconit(GlowChunk.Key key, Player p) {
        if (!chunkList.containsKey(key)) {
            chunkList.putIfAbsent(key, new Dyconit(p));
        } else {
            Dyconit dyconit = chunkList.get(key);
            dyconit.addSubscription(p);
        }
    }
}
