package net.glowstone.messagetype;

import com.flowpowered.network.Message;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.net.GlowSession;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class MessageFactory {
    private MessageFactory() {}

    public static UpdateMessage createMessageObject(Player player,
                                                    Message m,
                                                    GlowSession session,
                                                    Entity entity,
                                                    GlowChunk.Key key) {

        if (key == null && entity == null) {
            GlowChunk.Key k = getChunkKey(player);
            return new PlayerInformationUpdate(player, m, session, k);
        } else if (key == null) {
            GlowChunk.Key k = getChunkKey(entity);
            return new EntityInformationUpdate(player, m, session, entity, k);
        } else {
            return new ChunkInformationUpdate(player, m, session, key);
        }
    }

    private static GlowChunk.Key getChunkKey(Player p) {
        return GlowChunk.Key.of(p.getChunk().getX(), p.getChunk().getZ());
    }

    private static GlowChunk.Key getChunkKey(Entity e) {
        return GlowChunk.Key.of(e.getChunk().getX(), e.getChunk().getZ());
    }

}
