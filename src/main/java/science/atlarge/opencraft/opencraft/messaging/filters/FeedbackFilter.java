package science.atlarge.opencraft.opencraft.messaging.filters;

import com.flowpowered.network.Message;
import java.util.Map;
import java.util.function.Function;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityEquipmentMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityHeadRotationMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityMetadataMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityRotationMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityTeleportMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityVelocityMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.RelativeEntityPositionMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.RelativeEntityPositionRotationMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SetPassengerMessage;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnPlayerMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.BlockBreakAnimationMessage;
import science.atlarge.opencraft.opencraft.net.message.play.player.UseBedMessage;
import org.bukkit.entity.Player;
import science.atlarge.opencraft.messaging.Filter;

/**
 * The feedback filter prevents players from receiving messages the Minecraft client does not expect. This prevents a
 * feedback loop that would cause player movement to jitter.
 */
public final class FeedbackFilter implements Filter<Player, Message> {

    private final Map<Class<? extends Message>, Function<Message, Integer>> getters;

    /**
     * Create a feedback filter.
     */
    public FeedbackFilter() {
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
