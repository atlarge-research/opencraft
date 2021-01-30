package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.UseBedMessage;

public class UseBedCodecTest extends CodecTest<UseBedMessage> {

    @Override
    protected Codec<UseBedMessage> createCodec() {
        return new UseBedCodec();
    }

    @Override
    protected UseBedMessage createMessage() {
        return new UseBedMessage(1, 2, 3, 4);
    }
}
