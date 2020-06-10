package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.inv.EnchantItemMessage;

public class EnchantItemCodecTest extends CodecTest<EnchantItemMessage> {

    @Override
    protected Codec<EnchantItemMessage> createCodec() {
        return new EnchantItemCodec();
    }

    @Override
    protected EnchantItemMessage createMessage() {
        return new EnchantItemMessage(1, 2);
    }
}
