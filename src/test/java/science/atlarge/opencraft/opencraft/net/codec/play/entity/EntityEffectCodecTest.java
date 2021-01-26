package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityEffectMessage;

public class EntityEffectCodecTest extends CodecTest<EntityEffectMessage> {

    @Override
    protected Codec<EntityEffectMessage> createCodec() {
        return new EntityEffectCodec();
    }

    @Override
    protected EntityEffectMessage createMessage() {
        return new EntityEffectMessage(1, 2, 3, 4, false);
    }
}
