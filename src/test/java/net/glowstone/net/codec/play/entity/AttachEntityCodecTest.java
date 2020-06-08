package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.AttachEntityMessage;

public class AttachEntityCodecTest extends CodecTest<AttachEntityMessage> {

    @Override
    protected Codec<AttachEntityMessage> createCodec() {
        return new AttachEntityCodec();
    }

    @Override
    protected AttachEntityMessage createMessage() {
        return new AttachEntityMessage(1, 2);
    }
}
