package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.InteractEntityMessage;

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
