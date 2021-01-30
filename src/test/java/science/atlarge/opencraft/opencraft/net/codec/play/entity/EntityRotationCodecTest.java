package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityRotationMessage;

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
