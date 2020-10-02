package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.RespawnMessage;

public class RespawnCodecTest extends CodecTest<RespawnMessage> {

    @Override
    protected Codec<RespawnMessage> createCodec() {
        return new RespawnCodec();
    }

    @Override
    protected RespawnMessage createMessage() {
        return new RespawnMessage(1, 2, 3, "four");
    }
}
