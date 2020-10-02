package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.inv.WindowClickMessage;
import org.bukkit.inventory.ItemStack;

public final class WindowClickCodec implements Codec<WindowClickMessage> {

    @Override
    public WindowClickMessage decode(ByteBuf buffer) {
        int id = buffer.readUnsignedByte();
        int slot = buffer.readShort();
        int button = buffer.readByte();
        int action = buffer.readShort();
        int mode = buffer.readByte();
        ItemStack item = GlowBufUtils.readSlot(buffer, true);
        return new WindowClickMessage(id, slot, button, action, mode, item);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, WindowClickMessage message) {
        buffer.writeByte(message.getId());
        buffer.writeShort(message.getSlot());
        buffer.writeByte(message.getButton());
        buffer.writeShort(message.getTransaction());
        buffer.writeByte(message.getMode());
        GlowBufUtils.writeSlot(buffer, message.getItem());
        return buffer;
    }
}
