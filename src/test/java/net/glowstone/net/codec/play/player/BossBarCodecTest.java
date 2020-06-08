package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import java.util.UUID;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.BossBarMessage;

public class BossBarCodecTest extends CodecTest<BossBarMessage> {

    @Override
    protected Codec<BossBarMessage> createCodec() {
        return new BossBarCodec();
    }

    @Override
    protected BossBarMessage createMessage() {
        return new BossBarMessage(UUID.randomUUID(), BossBarMessage.Action.ADD, 1.0f);
    }
}
