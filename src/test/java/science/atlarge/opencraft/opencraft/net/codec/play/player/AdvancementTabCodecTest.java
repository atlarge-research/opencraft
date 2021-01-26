package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.AdvancementTabMessage;

public class AdvancementTabCodecTest extends CodecTest<AdvancementTabMessage> {

    @Override
    protected Codec<AdvancementTabMessage> createCodec() {
        return new AdvancementTabCodec();
    }

    @Override
    protected AdvancementTabMessage createMessage() {
        return new AdvancementTabMessage(AdvancementTabMessage.ACTION_OPEN, "tab");
    }
}
