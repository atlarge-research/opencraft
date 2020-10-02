package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.BlockPlacementMessage;

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
