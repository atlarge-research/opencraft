package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerUpdateMessage;

public final class PlayerUpdateCodec implements Codec<PlayerUpdateMessage> {

    @Override
    public PlayerUpdateMessage decode(ByteBuf buffer) {
        boolean onGround = buffer.readBoolean();
        return new PlayerUpdateMessage(onGround);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, PlayerUpdateMessage message) {
        buffer.writeBoolean(message.isOnGround());
        return buffer;
    }
}
