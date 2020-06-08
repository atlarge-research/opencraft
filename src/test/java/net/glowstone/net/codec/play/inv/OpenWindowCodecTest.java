package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.inv.OpenWindowMessage;

public class OpenWindowCodecTest extends CodecTest<OpenWindowMessage> {

    @Override
    protected Codec<OpenWindowMessage> createCodec() {
        return new OpenWindowCodec();
    }

    @Override
    protected OpenWindowMessage createMessage() {
        return new OpenWindowMessage(1, "two", "three", 4);
    }
}
