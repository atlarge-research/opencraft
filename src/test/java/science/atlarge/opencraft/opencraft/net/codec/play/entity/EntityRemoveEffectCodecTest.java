package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityRemoveEffectMessage;

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
