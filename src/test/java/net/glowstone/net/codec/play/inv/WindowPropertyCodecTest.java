package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.inv.WindowPropertyMessage;

public class WindowPropertyCodecTest extends CodecTest<WindowPropertyMessage> {

    @Override
    protected Codec<WindowPropertyMessage> createCodec() {
        return new WindowPropertyCodec();
    }

    @Override
    protected WindowPropertyMessage createMessage() {
        return new WindowPropertyMessage(1, 2, 3);
    }
}
