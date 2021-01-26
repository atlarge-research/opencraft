package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SetCooldownMessage;

public class SetCooldownCodecTest extends CodecTest<SetCooldownMessage> {

    @Override
    protected Codec<SetCooldownMessage> createCodec() {
        return new SetCooldownCodec();
    }

    @Override
    protected SetCooldownMessage createMessage() {
        return new SetCooldownMessage(1, 2);
    }
}
