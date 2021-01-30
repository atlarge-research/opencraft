package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.player.CombatEventMessage;
import science.atlarge.opencraft.opencraft.util.TextMessage;

public final class CombatEventCodec implements Codec<CombatEventMessage> {

    @Override
    public CombatEventMessage decode(ByteBuf buffer) throws IOException {
        int eventId = ByteBufUtils.readVarInt(buffer);
        CombatEventMessage.Event event = CombatEventMessage.Event.getAction(eventId);
        switch (event) {
            case END_COMBAT: {
                int duration = ByteBufUtils.readVarInt(buffer);
                int entityId = buffer.readInt();
                return new CombatEventMessage(event, duration, entityId);
            }
            case ENTITY_DEAD:
                int playerId = ByteBufUtils.readVarInt(buffer);
                int entityId = buffer.readInt();
                TextMessage message = GlowBufUtils.readChat(buffer);
                return new CombatEventMessage(event, playerId, entityId, message);
            default:
                return new CombatEventMessage(event);
        }
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, CombatEventMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getEvent().ordinal());
        if (message.getEvent() == CombatEventMessage.Event.END_COMBAT) {
            ByteBufUtils.writeVarInt(buffer, message.getDuration());
            buffer.writeInt(message.getEntityId());
        } else if (message.getEvent() == CombatEventMessage.Event.ENTITY_DEAD) {
            ByteBufUtils.writeVarInt(buffer, message.getPlayerId());
            buffer.writeInt(message.getEntityId());
            GlowBufUtils.writeChat(buffer, message.getMessage());
        }
        return buffer;
    }
}
