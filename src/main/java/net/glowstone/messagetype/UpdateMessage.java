package net.glowstone.messagetype;

import com.flowpowered.network.Message;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.net.GlowSession;
import org.bukkit.entity.Player;

public abstract class UpdateMessage {
    private final Player player;
    private final Message message;
    private final GlowSession session;
    private final GlowChunk.Key key;

    UpdateMessage(Player player,
                  Message message,
                  GlowSession session,
                  GlowChunk.Key key) {
        this.player = player;
        this.message = message;
        this.session = session;
        this.key = key;
    }

    public Player getPlayer() {
        return player;
    }

    public Message getMessage() {
        return message;
    }

    public GlowSession getSession() {
        return session;
    }

    public GlowChunk.Key getKey() {
        return key;
    }
}
