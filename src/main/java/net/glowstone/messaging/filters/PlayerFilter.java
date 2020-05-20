package net.glowstone.messaging.filters;

import com.flowpowered.network.Message;
import net.glowstone.messaging.Filter;
import net.glowstone.net.message.play.game.BlockBreakAnimationMessage;
import org.bukkit.entity.Player;

public class PlayerFilter implements Filter<Player, Message> {

    @Override
    public boolean filter(Player player, Message message) {
        return !(message instanceof BlockBreakAnimationMessage);
    }
}
