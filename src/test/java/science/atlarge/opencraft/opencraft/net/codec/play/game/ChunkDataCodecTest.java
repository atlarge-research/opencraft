package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.util.ArrayList;
import java.util.Collection;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.ChunkDataMessage;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;

public class ChunkDataCodecTest extends CodecTest<ChunkDataMessage> {

    @Override
    protected Codec<ChunkDataMessage> createCodec() {
        return new ChunkDataCodec();
    }

    @Override
    protected ChunkDataMessage createMessage() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        Collection<CompoundTag> tags = new ArrayList<>();
        tags.add(new CompoundTag());
        return new ChunkDataMessage(1, 2, false, 4, buffer, tags);
    }
}
