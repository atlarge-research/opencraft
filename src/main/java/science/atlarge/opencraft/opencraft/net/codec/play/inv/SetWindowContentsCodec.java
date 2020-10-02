package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.inv.SetWindowContentsMessage;
import org.bukkit.inventory.ItemStack;

public final class SetWindowContentsCodec implements Codec<SetWindowContentsMessage> {

    @Override
    public SetWindowContentsMessage decode(ByteBuf buffer) {
        byte id = buffer.readByte();
        short length = buffer.readShort();
        ItemStack[] items = new ItemStack[length];
        for (int index = 0; index < length; index++) {
            items[index] = GlowBufUtils.readSlot(buffer);
        }
        return new SetWindowContentsMessage(id, items);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SetWindowContentsMessage message) {
        buffer.writeByte(message.getId());
        buffer.writeShort(message.getItems().length);
        for (ItemStack item : message.getItems()) {
            GlowBufUtils.writeSlot(buffer, item);
        }
        return buffer;
    }
}
