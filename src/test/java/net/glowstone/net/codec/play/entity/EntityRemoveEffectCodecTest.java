package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.EntityRemoveEffectMessage;

public class EntityRemoveEffectCodecTest extends CodecTest<EntityRemoveEffectMessage> {

    @Override
    protected Codec<EntityRemoveEffectMessage> createCodec() {
        return new EntityRemoveEffectCodec();
    }

    @Override
    protected EntityRemoveEffectMessage createMessage() {
        return new EntityRemoveEffectMessage(1, 2);
    }
}
