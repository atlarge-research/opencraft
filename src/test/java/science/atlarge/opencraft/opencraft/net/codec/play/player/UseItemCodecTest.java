package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.UseItemMessage;

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
