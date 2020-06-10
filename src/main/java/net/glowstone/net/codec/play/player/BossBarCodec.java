package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import java.io.IOException;
import java.util.UUID;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.net.message.play.player.BossBarMessage;
import net.glowstone.net.message.play.player.BossBarMessage.Action;
import net.glowstone.net.message.play.player.BossBarMessage.Color;
import net.glowstone.net.message.play.player.BossBarMessage.Division;
import net.glowstone.util.TextMessage;

public class BossBarCodec implements Codec<BossBarMessage> {

    @Override
    public BossBarMessage decode(ByteBuf buffer) throws IOException {

        UUID uuid = GlowBufUtils.readUuid(buffer);
        Action action = Action.fromInt(ByteBufUtils.readVarInt(buffer));

        switch (action) {
            case ADD:
                TextMessage title = GlowBufUtils.readChat(buffer);
                float health = buffer.readFloat();
                Color color = Color.fromInt(ByteBufUtils.readVarInt(buffer));
                Division division = Division.fromInt(ByteBufUtils.readVarInt(buffer));
                byte flags = buffer.readByte();
                return new BossBarMessage(uuid, action, title, health, color, division, flags);
            case REMOVE:
                return new BossBarMessage(uuid, action);
            case UPDATE_HEALTH:
                health = buffer.readFloat();
                return new BossBarMessage(uuid, action, health);
            case UPDATE_TITLE:
                title = GlowBufUtils.readChat(buffer);
                return new BossBarMessage(uuid, action, title);
            case UPDATE_STYLE:
                color = Color.fromInt(ByteBufUtils.readVarInt(buffer));
                division = Division.fromInt(ByteBufUtils.readVarInt(buffer));
                return new BossBarMessage(uuid, action, color, division);
            case UPDATE_FLAGS:
                flags = buffer.readByte();
                return new BossBarMessage(uuid, action, flags);
            default:
                throw new DecoderException("Unsupported action: " + action);
        }
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, BossBarMessage message) throws IOException {

        GlowBufUtils.writeUuid(buffer, message.getUuid());
        ByteBufUtils.writeVarInt(buffer, message.getAction().ordinal());

        switch (message.getAction()) {
            case ADD:
                GlowBufUtils.writeChat(buffer, message.getTitle());
                buffer.writeFloat(message.getHealth());
                ByteBufUtils.writeVarInt(buffer, message.getColor().ordinal());
                ByteBufUtils.writeVarInt(buffer, message.getDivision().ordinal());
                buffer.writeByte(message.getFlags());
                break;
            case UPDATE_HEALTH:
                buffer.writeFloat(message.getHealth());
                break;
            case UPDATE_TITLE:
                GlowBufUtils.writeChat(buffer, message.getTitle());
                break;
            case UPDATE_STYLE:
                ByteBufUtils.writeVarInt(buffer, message.getColor().ordinal());
                ByteBufUtils.writeVarInt(buffer, message.getDivision().ordinal());
                break;
            case UPDATE_FLAGS:
                buffer.writeByte(message.getFlags());
                break;
            default:
                // do nothing
        }
        return buffer;
    }


}
