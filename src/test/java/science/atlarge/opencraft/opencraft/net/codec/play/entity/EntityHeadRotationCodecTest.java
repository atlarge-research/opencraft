package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityHeadRotationMessage;

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
