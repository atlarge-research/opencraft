package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.game.UserListHeaderFooterMessage;
import science.atlarge.opencraft.opencraft.util.TextMessage;

public final class UserListHeaderFooterCodec implements Codec<UserListHeaderFooterMessage> {

    @Override
    public UserListHeaderFooterMessage decode(ByteBuf buffer) throws IOException {
        TextMessage header = GlowBufUtils.readChat(buffer);
        TextMessage footer = GlowBufUtils.readChat(buffer);
        return new UserListHeaderFooterMessage(header, footer);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, UserListHeaderFooterMessage message) throws IOException {
        GlowBufUtils.writeChat(buffer, message.getHeader());
        GlowBufUtils.writeChat(buffer, message.getFooter());
        return buffer;
    }
}
