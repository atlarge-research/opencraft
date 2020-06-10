package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.net.message.play.inv.SetWindowSlotMessage;
import org.bukkit.inventory.ItemStack;

public final class SetWindowSlotCodec implements Codec<SetWindowSlotMessage> {

    @Override
    public SetWindowSlotMessage decode(ByteBuf buffer) {
        byte id = buffer.readByte();
        short slot = buffer.readShort();
        ItemStack item = GlowBufUtils.readSlot(buffer);
        return new SetWindowSlotMessage(id, slot, item);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SetWindowSlotMessage message) {
        buffer.writeByte(message.getId());
        buffer.writeShort(message.getSlot());
        GlowBufUtils.writeSlot(buffer, message.getItem());
        return buffer;
    }
}
