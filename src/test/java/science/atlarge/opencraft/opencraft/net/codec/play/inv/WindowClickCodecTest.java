package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.MockedCodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.inv.WindowClickMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WindowClickCodecTest extends MockedCodecTest<WindowClickMessage> {

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
