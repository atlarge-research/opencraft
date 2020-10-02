package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.WorldBorderMessage;

public class WorldBorderCodecTest extends CodecTest<WorldBorderMessage> {

    @Override
    protected Codec<WorldBorderMessage> createCodec() {
        return new WorldBorderCodec();
    }

    @Override
    protected WorldBorderMessage createMessage() {
        return new WorldBorderMessage(WorldBorderMessage.Action.SET_WARNING_BLOCKS, 2);
    }
}
