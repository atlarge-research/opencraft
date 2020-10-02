package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.CollectItemMessage;

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
