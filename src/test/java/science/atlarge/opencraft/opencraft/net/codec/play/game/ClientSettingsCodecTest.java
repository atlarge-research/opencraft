package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.ClientSettingsMessage;

public class ClientSettingsCodecTest extends CodecTest<ClientSettingsMessage> {

    @Override
    protected Codec<ClientSettingsMessage> createCodec() {
        return new ClientSettingsCodec();
    }

    @Override
    protected ClientSettingsMessage createMessage() {
        return new ClientSettingsMessage("one", 2, 3, true, 5, 6);
    }
}
