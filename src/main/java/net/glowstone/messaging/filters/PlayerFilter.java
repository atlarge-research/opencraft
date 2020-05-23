package net.glowstone.messaging.filters;

import com.flowpowered.network.Message;
import net.glowstone.messaging.Filter;
import net.glowstone.net.message.play.entity.EntityEquipmentMessage;
import net.glowstone.net.message.play.entity.EntityHeadRotationMessage;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import net.glowstone.net.message.play.entity.EntityRotationMessage;
import net.glowstone.net.message.play.entity.EntityTeleportMessage;
import net.glowstone.net.message.play.entity.EntityVelocityMessage;
import net.glowstone.net.message.play.entity.RelativeEntityPositionMessage;
import net.glowstone.net.message.play.entity.RelativeEntityPositionRotationMessage;
import net.glowstone.net.message.play.entity.SetPassengerMessage;
import net.glowstone.net.message.play.game.BlockBreakAnimationMessage;
import org.bukkit.entity.Player;

/**
 * The player filter prevents players from receiving messages the Minecraft client does not expect.
 */
public class PlayerFilter implements Filter<Player, Message> {

    @Override
    public boolean filter(Player player, Message message) {

        int id = -1;

        if (message instanceof BlockBreakAnimationMessage) {
            id = ((BlockBreakAnimationMessage) message).getId();

        } else if (message instanceof EntityTeleportMessage) {
            id = ((EntityTeleportMessage) message).getId();

        } else if (message instanceof RelativeEntityPositionRotationMessage) {
            id = ((RelativeEntityPositionRotationMessage) message).getId();

        } else if (message instanceof EntityRotationMessage) {
            id = ((EntityRotationMessage) message).getId();

        } else if (message instanceof RelativeEntityPositionMessage) {
            id = ((RelativeEntityPositionMessage) message).getId();

        } else if (message instanceof EntityMetadataMessage) { // Spawn
            id = ((EntityMetadataMessage) message).getId();

        } else if (message instanceof EntityEquipmentMessage) { // Spawn
            id = ((EntityEquipmentMessage) message).getId();

        } else if (message instanceof EntityHeadRotationMessage) { // Spawn
            id = ((EntityHeadRotationMessage) message).getId();

        } else if (message instanceof EntityVelocityMessage) { // Spawn + setVelocity
            id = ((EntityVelocityMessage) message).getId();

        } else if (message instanceof SetPassengerMessage) { // Pulse
            id = ((SetPassengerMessage) message).getEntityId();
        }

        return id != player.getEntityId();
    }
}
