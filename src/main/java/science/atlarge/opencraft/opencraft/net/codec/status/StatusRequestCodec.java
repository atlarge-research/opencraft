package science.atlarge.opencraft.opencraft.net.codec.status;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.status.StatusRequestMessage;

public final class StatusRequestCodec implements Codec<StatusRequestMessage> {

    @Override
    public StatusRequestMessage decode(ByteBuf buffer) {
        return new StatusRequestMessage();
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, StatusRequestMessage statusRequestMessage) {
        return buffer;
    }
}
