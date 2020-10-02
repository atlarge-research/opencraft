package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.EntityRotationMessage;

public class EntityRotationCodecTest extends CodecTest<EntityRotationMessage> {

    @Override
    protected Codec<EntityRotationMessage> createCodec() {
        return new EntityRotationCodec();
    }

    @Override
    protected EntityRotationMessage createMessage() {
        return new EntityRotationMessage(1, 2, 3, true);
    }
}
