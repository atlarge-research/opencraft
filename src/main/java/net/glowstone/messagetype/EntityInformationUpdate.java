package net.glowstone.messagetype;

import com.flowpowered.network.Message;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.net.GlowSession;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

class EntityInformationUpdate extends UpdateMessage {
    private Entity entity;

    EntityInformationUpdate(Player player,
                            Message message,
                            GlowSession session,
                            Entity entity,
                            GlowChunk.Key key) {
        super(player, message, session, key);
        this.entity = entity;
    }
}
