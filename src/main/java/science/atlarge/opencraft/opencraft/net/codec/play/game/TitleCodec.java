package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.game.TitleMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.TitleMessage.Action;
import science.atlarge.opencraft.opencraft.util.TextMessage;

public final class TitleCodec implements Codec<TitleMessage> {

    @Override
    public TitleMessage decode(ByteBuf buffer) throws IOException {
        int actionId = ByteBufUtils.readVarInt(buffer);
        Action action = Action.getAction(actionId);
        switch (action) {
            case TITLE:
            case SUBTITLE:
            case ACTION:
                String text = ByteBufUtils.readUTF8(buffer);
                return new TitleMessage(action, TextMessage.decode(text));
            case TIMES:
                int fadeIn = buffer.readInt();
                int stay = buffer.readInt();
                int fadeOut = buffer.readInt();
                return new TitleMessage(action, fadeIn, stay, fadeOut);
            default:
                return new TitleMessage(action);
        }
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, TitleMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getAction().ordinal());
        switch (message.getAction()) {
            case TITLE:
            case SUBTITLE:
            case ACTION:
                ByteBufUtils.writeUTF8(buffer, message.getText().encode());
                break;
            case TIMES:
                buffer.writeInt(message.getFadeIn());
                buffer.writeInt(message.getStay());
                buffer.writeInt(message.getFadeOut());
                break;
            default:
                // do nothing
        }
        return buffer;
    }
}
