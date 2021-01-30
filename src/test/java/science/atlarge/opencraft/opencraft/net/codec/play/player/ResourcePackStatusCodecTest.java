package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.ResourcePackStatusMessage;

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
