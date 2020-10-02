package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.inv.EnchantItemMessage;

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
