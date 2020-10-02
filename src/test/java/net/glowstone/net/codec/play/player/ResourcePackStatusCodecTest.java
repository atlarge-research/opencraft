package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.ResourcePackStatusMessage;

public class ResourcePackStatusCodecTest extends CodecTest<ResourcePackStatusMessage> {

    @Override
    protected Codec<ResourcePackStatusMessage> createCodec() {
        return new ResourcePackStatusCodec();
    }

    @Override
    protected ResourcePackStatusMessage createMessage() {
        return new ResourcePackStatusMessage(1);
    }
}
