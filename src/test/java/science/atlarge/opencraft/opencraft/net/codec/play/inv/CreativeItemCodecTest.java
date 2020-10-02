package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.MockedCodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.inv.CreativeItemMessage;
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
