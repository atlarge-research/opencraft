package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerPositionLookMessage;

public final class PlayerPositionLookCodec implements Codec<PlayerPositionLookMessage> {

    @Override
    public PlayerPositionLookMessage decode(ByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        float yaw = buffer.readFloat();
        float pitch = buffer.readFloat();
        boolean onGround = buffer.readBoolean();

        return new PlayerPositionLookMessage(onGround, x, y, z, yaw, pitch);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, PlayerPositionLookMessage message) {
        buffer.writeDouble(message.getX());
        buffer.writeDouble(message.getY());
        buffer.writeDouble(message.getZ());
        buffer.writeFloat(message.getYaw());
        buffer.writeFloat(message.getPitch());
        buffer.writeBoolean(message.isOnGround());
        return buffer;
    }
}
