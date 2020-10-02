package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityStatusMessage;

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
