package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.TeleportConfirmMessage;

public class TeleportConfirmCodecTest extends CodecTest<TeleportConfirmMessage> {

    @Override
    protected Codec<TeleportConfirmMessage> createCodec() {
        return new TeleportConfirmCodec();
    }

    @Override
    protected TeleportConfirmMessage createMessage() {
        return new TeleportConfirmMessage(1);
    }
}
