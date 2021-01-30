package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.inv.EnchantItemMessage;

public final class EnchantItemCodec implements Codec<EnchantItemMessage> {

    @Override
    public EnchantItemMessage decode(ByteBuf buffer) {
        int window = buffer.readByte();
        int enchantment = buffer.readByte();
        return new EnchantItemMessage(window, enchantment);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, EnchantItemMessage message) {
        buffer.writeByte(message.getWindow());
        buffer.writeByte(message.getEnchantment());
        return buffer;
    }
}
