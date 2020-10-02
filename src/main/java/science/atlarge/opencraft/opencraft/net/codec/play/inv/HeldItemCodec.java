package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.inv.HeldItemMessage;

public final class HeldItemCodec implements Codec<HeldItemMessage> {

    @Override
    public HeldItemMessage decode(ByteBuf buffer) {
        int slot = buffer.readShort();
        return new HeldItemMessage(slot);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, HeldItemMessage message) {
        buffer.writeByte(message.getSlot());
        return buffer;
    }
}
