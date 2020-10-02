package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.game.TimeMessage;

public final class TimeCodec implements Codec<TimeMessage> {

    @Override
    public TimeMessage decode(ByteBuf buffer) {
        long worldAge = buffer.readLong();
        long time = buffer.readLong();

        return new TimeMessage(worldAge, time);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, TimeMessage message) {
        buffer.writeLong(message.getWorldAge());
        buffer.writeLong(message.getTime());
        return buffer;
    }
}
