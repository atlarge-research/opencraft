package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.game.StateChangeMessage;

public final class StateChangeCodec implements Codec<StateChangeMessage> {

    @Override
    public StateChangeMessage decode(ByteBuf buffer) {
        int reason = buffer.readByte();
        float value = buffer.readFloat();

        return new StateChangeMessage(reason, value);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, StateChangeMessage message) {
        buffer.writeByte(message.getReason());
        buffer.writeFloat(message.getValue());
        return buffer;
    }
}
