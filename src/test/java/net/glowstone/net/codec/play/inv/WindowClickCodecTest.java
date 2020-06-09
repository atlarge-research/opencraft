package net.glowstone.net.codec.play.inv;

import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import com.flowpowered.network.Codec;
import net.glowstone.entity.objects.GlowItem;
import net.glowstone.inventory.GlowItemFactory;
import net.glowstone.inventory.GlowMetaItem;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.inv.WindowClickMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Bukkit.class)
public class WindowClickCodecTest extends CodecTest<WindowClickMessage> {

    @Before
    public void setUp() {

        GlowItemFactory itemFactory = GlowItemFactory.instance();

        PowerMockito.mockStatic(Bukkit.class);
        when(Bukkit.getItemFactory()).thenReturn(itemFactory);
    }

    @Override
    protected Codec<WindowClickMessage> createCodec() {
        return new WindowClickCodec();
    }

    @Override
    protected WindowClickMessage createMessage() {
        ItemStack item = new ItemStack(Material.DIRT, 2);
        return new WindowClickMessage(1, 2, 3, 4, 5, item);
    }
}
