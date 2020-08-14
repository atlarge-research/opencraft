package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.EntityStatusMessage;

public class EntityStatusCodecTest extends CodecTest<EntityStatusMessage> {

    @Override
    protected Codec<EntityStatusMessage> createCodec() {
        return new EntityStatusCodec();
    }

    @Override
    protected EntityStatusMessage createMessage() {
        return new EntityStatusMessage(1, EntityStatusMessage.ANIMAL_HEARTS);
    }
}
