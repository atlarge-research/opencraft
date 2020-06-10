package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.MockedCodecTest;
import net.glowstone.net.message.play.inv.CreativeItemMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CreativeItemCodecTest extends MockedCodecTest<CreativeItemMessage> {

    @Override
    protected Codec<CreativeItemMessage> createCodec() {
        return new CreativeItemCodec();
    }

    @Override
    protected CreativeItemMessage createMessage() {
        ItemStack item = new ItemStack(Material.DIRT, 1);
        return new CreativeItemMessage(1, item);
    }
}
