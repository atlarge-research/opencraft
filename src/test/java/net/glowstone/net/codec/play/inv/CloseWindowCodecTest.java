package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.inv.CloseWindowMessage;

public class CloseWindowCodecTest extends CodecTest<CloseWindowMessage> {

    @Override
    protected Codec<CloseWindowMessage> createCodec() {
        return new CloseWindowCodec();
    }

    @Override
    protected CloseWindowMessage createMessage() {
        return new CloseWindowMessage(1);
    }
}
