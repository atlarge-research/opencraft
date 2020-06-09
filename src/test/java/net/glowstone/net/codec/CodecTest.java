package net.glowstone.net.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.io.IOException;
import org.junit.Test;

public abstract class CodecTest<Message extends com.flowpowered.network.Message> {

    private static final ByteBufAllocator ALLOCATOR = UnpooledByteBufAllocator.DEFAULT;

    protected abstract Codec<Message> createCodec();

    protected abstract Message createMessage();

    @Test
    public void encodeAndDecode() throws IOException {
        Codec<Message> codec = createCodec();
        Message original = createMessage();
        ByteBuf encoded = ALLOCATOR.buffer();
        codec.encode(encoded, original);
        Message decoded = codec.decode(encoded);
        assertEquals(original, decoded, "Expected the decoded message to be equal to the original message");
    }
}
