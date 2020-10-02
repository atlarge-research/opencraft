package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.DiggingMessage;

public class DiggingCodecTest extends CodecTest<DiggingMessage> {

    @Override
    protected Codec<DiggingMessage> createCodec() {
        return new DiggingCodec();
    }

    @Override
    protected DiggingMessage createMessage() {
        return new DiggingMessage(1, 2, 3, 4, 5);
    }
}
