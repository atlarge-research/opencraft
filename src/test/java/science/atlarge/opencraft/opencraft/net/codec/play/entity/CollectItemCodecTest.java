package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.CollectItemMessage;

public class CollectItemCodecTest extends CodecTest<CollectItemMessage> {

    @Override
    protected Codec<CollectItemMessage> createCodec() {
        return new CollectItemCodec();
    }

    @Override
    protected CollectItemMessage createMessage() {
        return new CollectItemMessage(1, 2, 3);
    }
}
