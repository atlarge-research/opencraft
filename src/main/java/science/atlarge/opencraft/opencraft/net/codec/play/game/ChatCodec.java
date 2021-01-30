package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.game.ChatMessage;
import science.atlarge.opencraft.opencraft.util.TextMessage;

public final class ChatCodec implements Codec<ChatMessage> {

    @Override
    public ChatMessage decode(ByteBuf buffer) throws IOException {
        TextMessage message = GlowBufUtils.readChat(buffer);
        int mode = buffer.readByte();
        return new ChatMessage(message, mode);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ChatMessage message) throws IOException {
        GlowBufUtils.writeChat(buffer, message.getText());
        buffer.writeByte(message.getMode());
        return buffer;
    }
}
