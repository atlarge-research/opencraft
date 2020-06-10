package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.ClientSettingsMessage;

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
