package net.glowstone.messaging.filters;

import com.flowpowered.network.Message;
import java.util.Map;
import java.util.function.Function;
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
import net.glowstone.net.message.play.entity.SpawnPlayerMessage;
import net.glowstone.net.message.play.game.BlockBreakAnimationMessage;
import net.glowstone.net.message.play.player.UseBedMessage;
import org.bukkit.entity.Player;

/**
 * The player filter prevents players from receiving messages the Minecraft client does not expect.
 */
public final class PlayerFilter implements Filter<Player, Message> {

    private final Map<Class<? extends Message>, Function<Message, Integer>> getters;

    /**
     * Create a player filter.
     */
    public PlayerFilter() {
        getters = new ClassToGetterMapBuilder<Message, Integer>()
                .put(BlockBreakAnimationMessage.class, BlockBreakAnimationMessage::getId)
                .put(EntityTeleportMessage.class, EntityTeleportMessage::getId)
                .put(RelativeEntityPositionRotationMessage.class, RelativeEntityPositionRotationMessage::getId)
                .put(EntityRotationMessage.class, EntityRotationMessage::getId)
                .put(RelativeEntityPositionMessage.class, RelativeEntityPositionMessage::getId)
                .put(EntityMetadataMessage.class, EntityMetadataMessage::getId)
                .put(EntityEquipmentMessage.class, EntityEquipmentMessage::getId)
                .put(EntityHeadRotationMessage.class, EntityHeadRotationMessage::getId)
                .put(SpawnPlayerMessage.class, SpawnPlayerMessage::getId)
                .put(UseBedMessage.class, UseBedMessage::getId)
                .put(EntityVelocityMessage.class, EntityVelocityMessage::getId)
                .put(SetPassengerMessage.class, SetPassengerMessage::getEntityId)
                .build();
    }

    @Override
    public boolean filter(Player player, Message message) {
        Class<?> type = message.getClass();
        Function<Message, Integer> getter = getters.get(type);
        if (getter != null) {
            int playerId = player.getEntityId();
            int authorId = getter.apply(message);
            return playerId != authorId;
        }
        return true;
    }
}
