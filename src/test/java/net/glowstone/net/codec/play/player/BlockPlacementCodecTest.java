package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.BlockPlacementMessage;

public class BlockPlacementCodecTest extends CodecTest<BlockPlacementMessage> {

    @Override
    protected Codec<BlockPlacementMessage> createCodec() {
        return new BlockPlacementCodec();
    }

    @Override
    protected BlockPlacementMessage createMessage() {
        return new BlockPlacementMessage(1, 2, 3, 4, 5, 6.0f, 7.0f, 8.0f);
    }
}
