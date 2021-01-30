package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.UserListHeaderFooterMessage;
import science.atlarge.opencraft.opencraft.util.TextMessage;

public class UserListHeaderFooterCodecTest extends CodecTest<UserListHeaderFooterMessage> {

    @Override
    protected Codec<UserListHeaderFooterMessage> createCodec() {
        return new UserListHeaderFooterCodec();
    }

    @Override
    protected UserListHeaderFooterMessage createMessage() {
        TextMessage header = new TextMessage("one");
        TextMessage footer = new TextMessage("two");
        return new UserListHeaderFooterMessage(header, footer);
    }
}
