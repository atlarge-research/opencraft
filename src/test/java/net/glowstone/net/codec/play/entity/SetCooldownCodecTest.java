package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.SetCooldownMessage;

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
