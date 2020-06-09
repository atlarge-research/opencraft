package net.glowstone.net.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.io.IOException;
import net.glowstone.inventory.GlowItemFactory;
import org.bukkit.Bukkit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Bukkit.class)
public abstract class MockedCodecTest<Message extends com.flowpowered.network.Message> {

    private static final ByteBufAllocator ALLOCATOR = UnpooledByteBufAllocator.DEFAULT;

    protected abstract Codec<Message> createCodec();

    protected abstract Message createMessage();

    @Before
    public void setUp() {

        GlowItemFactory itemFactory = GlowItemFactory.instance();

        PowerMockito.mockStatic(Bukkit.class);
        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
    }

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
