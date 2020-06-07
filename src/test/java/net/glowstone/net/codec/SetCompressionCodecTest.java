package net.glowstone.net.codec;

import com.flowpowered.network.Codec;
import net.glowstone.net.message.SetCompressionMessage;

public class SetCompressionCodecTest extends CodecTest<SetCompressionMessage> {

    @Override
    protected Codec<SetCompressionMessage> createCodec() {
        return new SetCompressionCodec();
    }

    @Override
    protected SetCompressionMessage createMessage() {
        return new SetCompressionMessage(1);
    }
}
