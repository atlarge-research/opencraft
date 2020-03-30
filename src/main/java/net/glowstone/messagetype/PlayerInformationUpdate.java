package net.glowstone.messagetype;

import com.flowpowered.network.Message;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.net.GlowSession;
import org.bukkit.entity.Player;

class PlayerInformationUpdate extends UpdateMessage {
    PlayerInformationUpdate(Player player,
                            Message message,
                            GlowSession session,
                            GlowChunk.Key key) {
        super(player, message, session, key);
    }
}
