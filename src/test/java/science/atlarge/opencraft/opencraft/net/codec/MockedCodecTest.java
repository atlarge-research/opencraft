package science.atlarge.opencraft.opencraft.net.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.inventory.GlowItemFactory;
import org.bukkit.Bukkit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * The MockedCodecTest class tests functionality that all codecs should be able to provide. However, it runs these
 * tests using the PowerMockRunner, such that a static method in the Glowkit library can be mocked.
 *
 * @param <Message> the type of message that should be encoded and decoded by the codec.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Bukkit.class)
public abstract class MockedCodecTest<Message extends com.flowpowered.network.Message> {

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
     * Setup the mocked getItemFactory method required for testing certain codecs.
     */
    @Before
    public void setUp() {

        GlowItemFactory itemFactory = GlowItemFactory.instance();

        PowerMockito.mockStatic(Bukkit.class);
        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
    }

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
