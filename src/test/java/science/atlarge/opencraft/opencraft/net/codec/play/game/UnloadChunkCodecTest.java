package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.UnloadChunkMessage;

public class UnloadChunkCodecTest extends CodecTest<UnloadChunkMessage> {

    @Override
    protected Codec<UnloadChunkMessage> createCodec() {
        return new UnloadChunkCodec();
    }

    @Override
    protected UnloadChunkMessage createMessage() {
        return new UnloadChunkMessage(1, 2);
    }
}
