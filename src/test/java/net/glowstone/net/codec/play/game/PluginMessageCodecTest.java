package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.PluginMessage;

public class PluginMessageCodecTest extends CodecTest<PluginMessage> {

    @Override
    protected Codec<PluginMessage> createCodec() {
        return new PluginMessageCodec();
    }

    @Override
    protected PluginMessage createMessage() {
        return new PluginMessage("one", new byte[] {1, 2});
    }
}
