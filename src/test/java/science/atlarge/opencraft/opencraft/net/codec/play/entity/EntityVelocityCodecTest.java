package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityVelocityMessage;

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
