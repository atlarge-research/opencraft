package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.PluginMessage;

public class PluginMessageCodecTest extends CodecTest<PluginMessage> {

    @Override
    protected Codec<PluginMessage> createCodec() {
        return new PluginMessageCodec();
    }

    @Override
    protected PluginMessage createMessage() {
        return new PluginMessage("one", new byte[] { 1, 2 });
    }
}
