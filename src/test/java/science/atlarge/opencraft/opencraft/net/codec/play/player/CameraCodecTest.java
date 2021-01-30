package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.CameraMessage;

public class CameraCodecTest extends CodecTest<CameraMessage> {

    @Override
    protected Codec<CameraMessage> createCodec() {
        return new CameraCodec();
    }

    @Override
    protected CameraMessage createMessage() {
        return new CameraMessage(1);
    }
}
