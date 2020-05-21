package net.glowstone.messaging.filters;

import com.flowpowered.network.Message;
import net.glowstone.messaging.Filter;
import net.glowstone.net.message.play.game.BlockBreakAnimationMessage;
import org.bukkit.entity.Player;

/**
 * The player filter prevents players from receiving messages the Minecraft client does not expect.
 */
public class PlayerFilter implements Filter<Player, Message> {

    @Override
    public boolean filter(Player player, Message message) {

        if (message instanceof BlockBreakAnimationMessage) {
            int authorId = ((BlockBreakAnimationMessage) message).getId();
            return authorId != player.getEntityId();
        }

        return true;
    }
}
