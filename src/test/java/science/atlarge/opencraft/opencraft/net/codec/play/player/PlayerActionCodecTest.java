package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerActionMessage;

public class PlayerActionCodecTest extends CodecTest<PlayerActionMessage> {

    @Override
    protected Codec<PlayerActionMessage> createCodec() {
        return new PlayerActionCodec();
    }

    @Override
    protected PlayerActionMessage createMessage() {
        return new PlayerActionMessage(1, 2, 3);
    }
}
