package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.EntityHeadRotationMessage;

public class EntityHeadRotationCodecTest extends CodecTest<EntityHeadRotationMessage> {

    @Override
    protected Codec<EntityHeadRotationMessage> createCodec() {
        return new EntityHeadRotationCodec();
    }

    @Override
    protected EntityHeadRotationMessage createMessage() {
        return new EntityHeadRotationMessage(1, 2);
    }
}
