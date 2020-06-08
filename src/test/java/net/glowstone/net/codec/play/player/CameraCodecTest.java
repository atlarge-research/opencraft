package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.CameraMessage;

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
