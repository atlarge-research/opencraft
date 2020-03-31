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
    private PolicyDonnybrook policy;

    public DyconitManager() {
        chunks = new DyconitCollection();
        policy = PolicyDonnybrook.setPolicy();
    }

    public void send(Player player, Message m, GlowSession session, Entity entity, GlowChunk.Key key) {
        //System.out.println(player.getChunk().toString());
        UpdateMessage message = MessageFactory.createMessageObject(player, m, session, entity, key);

        chunks.insertDyconit(message.getKey(), message.getPlayer());
        chunks.retrieveDyconit(message.getKey()).addMessage(player, m);

        policy.enforce(player);

        sendUpdatesToClient(player, session);
    }

    public void sendUpdatesToClient(Player p, GlowSession session) {
        for (Dyconit dyconit : chunks.getKeyDyconitMap().values()) {
            Dyconit.Subscription sub = dyconit.subscriptions.get(p);

            if (sub == null){ continue; }

            if (sub.exceedBound()) {
                sub.messageQueue.forEach(session::send);
                sub.messageQueue.clear();
            }
        }
    }

    static Set getNearbyChunkKeys(Player player, int distance) {
        System.out.println("BEGIN");
        Set<GlowChunk.Key> chunkKeys = new HashSet<>();

        Chunk currentChunk = player.getChunk();
        int xloc = currentChunk.getX();
        int zloc = currentChunk.getZ();

        System.out.println(xloc + ' ' + zloc);

        for (int i = -distance; i <= distance; i++) {
            for (int j = -distance; j <= distance; j++) {
                chunkKeys.add(GlowChunk.Key.of(xloc + i, zloc + j));
            }
        }
        return chunkKeys;
    }

    static DyconitCollection getDyconits() {
        return chunks;
    }
}
