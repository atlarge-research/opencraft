package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.MockedCodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.inv.SetWindowContentsMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SetWindowContentsCodecTest extends MockedCodecTest<SetWindowContentsMessage> {

    @Override
    protected Codec<SetWindowContentsMessage> createCodec() {
        return new SetWindowContentsCodec();
    }

    @Override
    protected SetWindowContentsMessage createMessage() {
        ItemStack[] items = new ItemStack[2];
        items[0] = new ItemStack(Material.DIRT, 3);
        items[1] = new ItemStack(Material.STONE, 5);
        return new SetWindowContentsMessage(1, items);
    }
}
