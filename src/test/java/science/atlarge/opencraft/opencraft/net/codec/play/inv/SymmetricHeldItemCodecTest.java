package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.inv.HeldItemMessage;

public class SymmetricHeldItemCodecTest extends CodecTest<HeldItemMessage> {

    @Override
    protected Codec<HeldItemMessage> createCodec() {
        return new SymmetricHeldItemCodec();
    }

    @Override
    protected HeldItemMessage createMessage() {
        return new HeldItemMessage(1);
    }
}
