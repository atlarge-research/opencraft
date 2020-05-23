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

        ClassGetterMapBuilder<Message, Integer> builder = new ClassGetterMapBuilder<>();
        builder.add(BlockBreakAnimationMessage.class, BlockBreakAnimationMessage::getId);
        builder.add(EntityTeleportMessage.class, EntityTeleportMessage::getId);
        builder.add(RelativeEntityPositionRotationMessage.class, RelativeEntityPositionRotationMessage::getId);
        builder.add(EntityRotationMessage.class, EntityRotationMessage::getId);
        builder.add(RelativeEntityPositionMessage.class, RelativeEntityPositionMessage::getId);
        builder.add(EntityMetadataMessage.class, EntityMetadataMessage::getId);
        builder.add(EntityEquipmentMessage.class, EntityEquipmentMessage::getId);
        builder.add(EntityHeadRotationMessage.class, EntityHeadRotationMessage::getId);
        builder.add(SpawnPlayerMessage.class, SpawnPlayerMessage::getId);
        builder.add(UseBedMessage.class, UseBedMessage::getId);
        builder.add(EntityVelocityMessage.class, EntityVelocityMessage::getId);
        builder.add(SetPassengerMessage.class, SetPassengerMessage::getEntityId);

        getters = builder.getImmutableMap();
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
