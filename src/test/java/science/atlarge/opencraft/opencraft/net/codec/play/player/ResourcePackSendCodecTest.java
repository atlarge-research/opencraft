package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.ResourcePackSendMessage;

public class ResourcePackSendCodecTest extends CodecTest<ResourcePackSendMessage> {

    @Override
    protected Codec<ResourcePackSendMessage> createCodec() {
        return new ResourcePackSendCodec();
    }

    @Override
    protected ResourcePackSendMessage createMessage() {
        return new ResourcePackSendMessage("one", "two");
    }
}
