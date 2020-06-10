package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.UseItemMessage;

public class UseItemCodecTest extends CodecTest<UseItemMessage> {

    @Override
    protected Codec<UseItemMessage> createCodec() {
        return new UseItemCodec();
    }

    @Override
    protected UseItemMessage createMessage() {
        return new UseItemMessage(1);
    }
}
