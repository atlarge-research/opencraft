package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.inv.WindowPropertyMessage;

public class WindowPropertyCodecTest extends CodecTest<WindowPropertyMessage> {

    @Override
    protected Codec<WindowPropertyMessage> createCodec() {
        return new WindowPropertyCodec();
    }

    @Override
    protected WindowPropertyMessage createMessage() {
        return new WindowPropertyMessage(1, 2, 3);
    }
}
