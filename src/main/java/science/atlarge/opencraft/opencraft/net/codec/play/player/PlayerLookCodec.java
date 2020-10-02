package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerLookMessage;

public final class PlayerLookCodec implements Codec<PlayerLookMessage> {

    @Override
    public PlayerLookMessage decode(ByteBuf buffer) {
        float yaw = buffer.readFloat();
        float pitch = buffer.readFloat();
        boolean onGround = buffer.readBoolean();
        return new PlayerLookMessage(yaw, pitch, onGround);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, PlayerLookMessage message) {
        buffer.writeFloat(message.getYaw());
        buffer.writeFloat(message.getPitch());
        buffer.writeBoolean(message.isOnGround());
        return buffer;
    }
}
