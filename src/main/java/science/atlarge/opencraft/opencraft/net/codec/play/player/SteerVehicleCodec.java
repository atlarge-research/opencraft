package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.player.SteerVehicleMessage;

public final class SteerVehicleCodec implements Codec<SteerVehicleMessage> {

    @Override
    public SteerVehicleMessage decode(ByteBuf buffer) {
        float sideways = buffer.readFloat();
        float forward = buffer.readFloat();
        int flags = buffer.readUnsignedByte();

        boolean jump = (flags & 0x1) != 0;
        boolean unmount = (flags & 0x2) != 0;
        return new SteerVehicleMessage(sideways, forward, jump, unmount);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SteerVehicleMessage message) {
        buffer.writeFloat(message.getSideways());
        buffer.writeFloat(message.getForward());
        buffer.writeByte((message.isJump() ? 1 : 0) | (message.isUnmount() ? 2 : 0));
        return buffer;
    }
}
