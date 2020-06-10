package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.inv.TransactionMessage;

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
