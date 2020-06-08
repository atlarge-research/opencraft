package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import java.util.Arrays;
import java.util.List;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.DestroyEntitiesMessage;

public class DestroyEntitiesCodecTest extends CodecTest<DestroyEntitiesMessage> {

    @Override
    protected Codec<DestroyEntitiesMessage> createCodec() {
        return new DestroyEntitiesCodec();
    }

    @Override
    protected DestroyEntitiesMessage createMessage() {
        List<Integer> ids = Arrays.asList(1, 2);
        return new DestroyEntitiesMessage(ids);
    }
}
