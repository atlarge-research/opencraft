package net.glowstone.net.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * The CodecTest class tests functionality that all codecs should be able to provide.
 *
 * @param <Message> the type of message that should be encoded and decoded by the codec.
 */
public abstract class CodecTest<Message extends com.flowpowered.network.Message> {

    private static final ByteBufAllocator ALLOCATOR = UnpooledByteBufAllocator.DEFAULT;

    /**
     * Create a new codec.
     *
     * @return the created codec.
     */
    protected abstract Codec<Message> createCodec();

    /**
     * Create a new message.
     *
     * @return the created message.
     */
    protected abstract Message createMessage();

    /**
     * Verify that a message that is encoded and then decoded is equal to its original.
     *
     * @throws IOException whenever an IOException occurs during either encoding or decoding.
     */
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
