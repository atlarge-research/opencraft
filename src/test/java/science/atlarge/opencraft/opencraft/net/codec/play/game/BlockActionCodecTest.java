package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.BlockActionMessage;

public class BlockActionCodecTest extends CodecTest<BlockActionMessage> {

    @Override
    protected Codec<BlockActionMessage> createCodec() {
        return new BlockActionCodec();
    }

    @Override
    protected BlockActionMessage createMessage() {
        return new BlockActionMessage(1, 2, 3, 4, 5, 6);
    }
}
