package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.TeleportConfirmMessage;

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
