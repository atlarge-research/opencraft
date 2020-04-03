package net.glowstone.dyconit;

import com.flowpowered.network.Message;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.messagetype.MessageFactory;
import net.glowstone.messagetype.UpdateMessage;
import net.glowstone.net.GlowSession;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.Set;

public class DyconitManager {
    public static DyconitCollection chunks;
    private IPolicy policy;

    public DyconitManager() {
        chunks = new DyconitCollection();
        policy = PolicyFactory.loadPolicy();
    }

    public void send(Player player,
                     Message m,
                     GlowSession session,
                     Entity entity,
                     GlowChunk.Key key) {


        UpdateMessage message = MessageFactory.createMessageObject(player, m, session, entity, key);

        chunks.insertDyconit(message.getKey(), message.getPlayer());
        chunks.retrieveDyconit(message.getKey()).addMessage(player, message);

        policy.enforce(player, chunks);
    }

    public void processDyconits() {
        for (Dyconit dyconit : chunks.getKeyDyconitMap().values()) {
            processSubscriptions(dyconit);
        }
    }

    private void processSubscriptions(Dyconit dyconit) {
        for (Player pSub : dyconit.subscriptions.keySet()) {
            if (dyconit.subscriptions.containsKey(pSub)) {
                Dyconit.Subscription sub = dyconit.subscriptions.get(pSub);
                if (sub.messageQueue.size() > 0) {
                    sendUpdatesToClient(sub);
                } else {
                    dyconit.subscriptions.remove(pSub);
                }
            }
        }
    }

    private void sendUpdatesToClient(Dyconit.Subscription sub) {
        if (sub.exceedBound()) {
            synchronized (sub.messageQueue) {
                sub.messageQueue.forEach(m -> m.getSession().send(m.getMessage()));
                sub.messageQueue.clear();
            }
        }
    }

    static Set<GlowChunk.Key> getNearbyChunkKeys(Player player, int distance) {
        Set<GlowChunk.Key> chunkKeys = new HashSet<>();

        Chunk currentChunk = player.getChunk();
        int xloc = currentChunk.getX();
        int zloc = currentChunk.getZ();

        for (int i = -distance; i <= distance; i++) {
            for (int j = -distance; j <= distance; j++) {
                chunkKeys.add(GlowChunk.Key.of(xloc + i, zloc + j));
            }
        }
        return chunkKeys;
    }
}
