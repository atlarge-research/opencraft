package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.game.ClientSettingsMessage;

public final class ClientSettingsCodec implements Codec<ClientSettingsMessage> {

    @Override
    public ClientSettingsMessage decode(ByteBuf buffer) throws IOException {
        String locale = ByteBufUtils.readUTF8(buffer);
        int viewDistance = buffer.readByte();
        int chatFlags = ByteBufUtils.readVarInt(buffer);
        boolean colors = buffer.readBoolean();
        int skinFlags = buffer.readUnsignedByte();
        int hand = ByteBufUtils.readVarInt(buffer);
        return new ClientSettingsMessage(locale, viewDistance, chatFlags, colors, skinFlags, hand);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ClientSettingsMessage message) throws IOException {
        ByteBufUtils.writeUTF8(buffer, message.getLocale());
        buffer.writeByte(message.getViewDistance());
        ByteBufUtils.writeVarInt(buffer, message.getChatFlags());
        buffer.writeBoolean(message.isChatColors());
        buffer.writeByte(message.getSkinFlags());
        ByteBufUtils.writeVarInt(buffer, message.getHand());
        return buffer;
    }
}
