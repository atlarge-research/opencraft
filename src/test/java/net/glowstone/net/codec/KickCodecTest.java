package net.glowstone.net.codec;

import com.flowpowered.network.Codec;
import net.glowstone.net.message.KickMessage;

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
