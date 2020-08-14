package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.ClientStatusMessage;

public class ClientStatusCodecTest extends CodecTest<ClientStatusMessage> {

    @Override
    protected Codec<ClientStatusMessage> createCodec() {
        return new ClientStatusCodec();
    }

    @Override
    protected ClientStatusMessage createMessage() {
        return new ClientStatusMessage(1);
    }
}
