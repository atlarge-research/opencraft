package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.BlockBreakAnimationMessage;

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
