package science.atlarge.opencraft.opencraft.net.codec;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.message.KickMessage;

public class KickCodecTest extends CodecTest<KickMessage> {

    @Override
    protected Codec<KickMessage> createCodec() {
        return new KickCodec();
    }

    @Override
    protected KickMessage createMessage() {
        return new KickMessage("one");
    }
}
