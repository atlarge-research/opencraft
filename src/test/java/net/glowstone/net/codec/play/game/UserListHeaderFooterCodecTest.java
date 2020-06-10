package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.UserListHeaderFooterMessage;
import net.glowstone.util.TextMessage;

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
