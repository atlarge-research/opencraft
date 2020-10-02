package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.ClientStatusMessage;

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
