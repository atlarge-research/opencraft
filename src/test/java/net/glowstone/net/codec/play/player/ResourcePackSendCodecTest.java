package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.ResourcePackSendMessage;

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
