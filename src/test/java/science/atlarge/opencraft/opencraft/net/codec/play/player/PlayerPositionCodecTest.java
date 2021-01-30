package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerPositionMessage;

public class PlayerPositionCodecTest extends CodecTest<PlayerPositionMessage> {

    @Override
    protected Codec<PlayerPositionMessage> createCodec() {
        return new PlayerPositionCodec();
    }

    @Override
    protected PlayerPositionMessage createMessage() {
        return new PlayerPositionMessage(false, 2, 3, 4);
    }
}
