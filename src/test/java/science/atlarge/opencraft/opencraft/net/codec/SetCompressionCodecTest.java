package science.atlarge.opencraft.opencraft.net.codec;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.message.SetCompressionMessage;

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
