package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.game.PingMessage;

public final class PingCodec implements Codec<PingMessage> {

    @Override
    public PingMessage decode(ByteBuf buffer) {
        return new PingMessage(buffer.readLong());
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, PingMessage message) {
        buffer.writeLong(message.getPingId());
        return buffer;
    }
}
