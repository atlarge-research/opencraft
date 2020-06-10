package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.EntityVelocityMessage;

public class EntityVelocityCodecTest extends CodecTest<EntityVelocityMessage> {

    @Override
    protected Codec<EntityVelocityMessage> createCodec() {
        return new EntityVelocityCodec();
    }

    @Override
    protected EntityVelocityMessage createMessage() {
        return new EntityVelocityMessage(1, 2, 3, 4);
    }
}
