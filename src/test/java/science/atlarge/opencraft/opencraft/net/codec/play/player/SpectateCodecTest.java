package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import java.util.UUID;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.SpectateMessage;

public class SpectateCodecTest extends CodecTest<SpectateMessage> {

    @Override
    protected Codec<SpectateMessage> createCodec() {
        return new SpectateCodec();
    }

    @Override
    protected SpectateMessage createMessage() {
        return new SpectateMessage(UUID.randomUUID());
    }
}
