package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import java.util.UUID;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnPaintingMessage;

public class SpawnPaintingCodecTest extends CodecTest<SpawnPaintingMessage> {

    @Override
    protected Codec<SpawnPaintingMessage> createCodec() {
        return new SpawnPaintingCodec();
    }

    @Override
    protected SpawnPaintingMessage createMessage() {
        return new SpawnPaintingMessage(1, UUID.randomUUID(), "three", 4, 5, 6, 7);
    }
}
