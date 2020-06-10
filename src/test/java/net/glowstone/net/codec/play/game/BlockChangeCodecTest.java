package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.BlockChangeMessage;

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
