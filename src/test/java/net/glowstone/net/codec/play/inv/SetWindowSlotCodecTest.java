package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.inv.SetWindowSlotMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SetWindowSlotCodecTest extends CodecTest<SetWindowSlotMessage> {

    @Override
    protected Codec<SetWindowSlotMessage> createCodec() {
        return new SetWindowSlotCodec();
    }

    @Override
    protected SetWindowSlotMessage createMessage() {
        ItemStack item = new ItemStack(Material.DIRT, 3);
        return new SetWindowSlotMessage(1, 2, item);
    }
}
