package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerPositionMessage;

public final class PlayerPositionCodec implements Codec<PlayerPositionMessage> {

    @Override
    public PlayerPositionMessage decode(ByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        boolean onGround = buffer.readBoolean();

        return new PlayerPositionMessage(onGround, x, y, z);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, PlayerPositionMessage message) {
        buffer.writeDouble(message.getX());
        buffer.writeDouble(message.getY());
        buffer.writeDouble(message.getZ());
        buffer.writeBoolean(message.isOnGround());
        return buffer;
    }
}
