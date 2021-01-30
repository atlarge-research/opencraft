package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerUpdateMessage;

public class PlayerUpdateCodecTest extends CodecTest<PlayerUpdateMessage> {

    @Override
    protected Codec<PlayerUpdateMessage> createCodec() {
        return new PlayerUpdateCodec();
    }

    @Override
    protected PlayerUpdateMessage createMessage() {
        return new PlayerUpdateMessage(false);
    }
}
