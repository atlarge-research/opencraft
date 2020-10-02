package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.BlockChangeMessage;

public class BlockChangeCodecTest extends CodecTest<BlockChangeMessage> {

    @Override
    protected Codec<BlockChangeMessage> createCodec() {
        return new BlockChangeCodec();
    }

    @Override
    protected BlockChangeMessage createMessage() {
        return new BlockChangeMessage(1, 2, 3, 4, 5);
    }
}
