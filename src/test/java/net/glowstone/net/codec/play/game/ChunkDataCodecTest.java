package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.text.html.HTML;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.ChunkDataMessage;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.Tag;

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
