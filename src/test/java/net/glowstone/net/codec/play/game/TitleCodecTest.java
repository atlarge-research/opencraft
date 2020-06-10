package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.TitleMessage;
import net.glowstone.util.TextMessage;

public class TitleCodecTest extends CodecTest<TitleMessage> {

    @Override
    protected Codec<TitleMessage> createCodec() {
        return new TitleCodec();
    }

    @Override
    protected TitleMessage createMessage() {
        return new TitleMessage(TitleMessage.Action.ACTION, new TextMessage("two"));
    }
}
