package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.InteractEntityMessage;

public class InteractEntityCodecTest extends CodecTest<InteractEntityMessage> {

    @Override
    protected Codec<InteractEntityMessage> createCodec() {
        return new InteractEntityCodec();
    }

    @Override
    protected InteractEntityMessage createMessage() {
        return new InteractEntityMessage(1, 2, 3.0f, 4.0f, 5.0f, 6);
    }
}
