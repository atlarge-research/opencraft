package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.PingMessage;

public class PingCodecTest extends CodecTest<PingMessage> {

    @Override
    protected Codec<PingMessage> createCodec() {
        return new PingCodec();
    }

    @Override
    protected PingMessage createMessage() {
        return new PingMessage(1);
    }
}
