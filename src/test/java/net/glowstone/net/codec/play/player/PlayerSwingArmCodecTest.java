package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.PlayerSwingArmMessage;

public class PlayerSwingArmCodecTest extends CodecTest<PlayerSwingArmMessage> {

    @Override
    protected Codec<PlayerSwingArmMessage> createCodec() {
        return new PlayerSwingArmCodec();
    }

    @Override
    protected PlayerSwingArmMessage createMessage() {
        return new PlayerSwingArmMessage(1);
    }
}
