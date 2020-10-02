package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.BlockActionMessage;

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
