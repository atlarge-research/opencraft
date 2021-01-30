package science.atlarge.opencraft.opencraft.net.codec.status;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.status.StatusPingMessage;

public final class StatusPingCodec implements Codec<StatusPingMessage> {

    @Override
    public StatusPingMessage decode(ByteBuf buffer) {
        return new StatusPingMessage(buffer.readLong());
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, StatusPingMessage statusPingMessage) {
        buffer.writeLong(statusPingMessage.getTime());
        return buffer;
    }
}
