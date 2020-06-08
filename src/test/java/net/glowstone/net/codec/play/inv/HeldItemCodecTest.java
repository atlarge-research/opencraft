package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.inv.HeldItemMessage;

public class HeldItemCodecTest extends CodecTest<HeldItemMessage> {

    @Override
    protected Codec<HeldItemMessage> createCodec() {
        return new HeldItemCodec();
    }

    @Override
    protected HeldItemMessage createMessage() {
        return new HeldItemMessage(1);
    }
}
