package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.player.SteerBoatMessage;

public class SteerBoatCodec implements Codec<SteerBoatMessage> {

    @Override
    public SteerBoatMessage decode(ByteBuf buffer) throws IOException {
        boolean isRightPaddleTurning = buffer.readBoolean();
        boolean isLeftPaddleTurning = buffer.readBoolean();
        return new SteerBoatMessage(isRightPaddleTurning, isLeftPaddleTurning);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SteerBoatMessage steerBoatMessage) throws IOException {
        buffer.writeBoolean(steerBoatMessage.isRightPaddleTurning());
        buffer.writeBoolean(steerBoatMessage.isLeftPaddleTurning());
        return buffer;
    }
}
