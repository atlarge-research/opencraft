package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerAbilitiesMessage;

public class PlayerAbilitiesCodecTest extends CodecTest<PlayerAbilitiesMessage> {

    @Override
    protected Codec<PlayerAbilitiesMessage> createCodec() {
        return new PlayerAbilitiesCodec();
    }

    @Override
    protected PlayerAbilitiesMessage createMessage() {
        return new PlayerAbilitiesMessage(1, 2.0f, 3.0f);
    }
}
