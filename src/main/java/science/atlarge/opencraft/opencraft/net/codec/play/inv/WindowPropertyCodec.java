package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.inv.WindowPropertyMessage;

public final class WindowPropertyCodec implements Codec<WindowPropertyMessage> {

    @Override
    public WindowPropertyMessage decode(ByteBuf buffer) {
        byte id = buffer.readByte();
        short property = buffer.readShort();
        short value = buffer.readShort();
        return new WindowPropertyMessage(id, property, value);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, WindowPropertyMessage message) {
        buffer.writeByte(message.getId());
        buffer.writeShort(message.getProperty());
        buffer.writeShort(message.getValue());
        return buffer;
    }
}
