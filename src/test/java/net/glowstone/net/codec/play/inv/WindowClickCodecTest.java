package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.inv.WindowClickMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WindowClickCodecTest extends CodecTest<WindowClickMessage> {

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
