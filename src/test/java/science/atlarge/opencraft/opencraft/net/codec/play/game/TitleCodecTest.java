package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.TitleMessage;
import science.atlarge.opencraft.opencraft.util.TextMessage;

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
