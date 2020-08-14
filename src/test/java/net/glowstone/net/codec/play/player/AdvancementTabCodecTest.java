package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.AdvancementTabMessage;

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
