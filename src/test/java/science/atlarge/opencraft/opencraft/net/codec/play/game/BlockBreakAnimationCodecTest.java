package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.BlockBreakAnimationMessage;

public class BlockBreakAnimationCodecTest extends CodecTest<BlockBreakAnimationMessage> {

    @Override
    protected Codec<BlockBreakAnimationMessage> createCodec() {
        return new BlockBreakAnimationCodec();
    }

    @Override
    protected BlockBreakAnimationMessage createMessage() {
        return new BlockBreakAnimationMessage(1, 2, 3, 4, 5);
    }
}
