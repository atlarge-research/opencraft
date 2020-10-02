package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.inv.TransactionMessage;

public class TransactionCodecTest extends CodecTest<TransactionMessage> {

    @Override
    protected Codec<TransactionMessage> createCodec() {
        return new TransactionCodec();
    }

    @Override
    protected TransactionMessage createMessage() {
        return new TransactionMessage(1, 2, false);
    }
}
