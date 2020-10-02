package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerSwingArmMessage;

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
