package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.player.PlayerActionMessage;

public final class PlayerActionCodec implements Codec<PlayerActionMessage> {

    @Override
    public PlayerActionMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        int action = buffer.readByte();
        int jumpBoost = ByteBufUtils.readVarInt(buffer);
        return new PlayerActionMessage(id, action, jumpBoost);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, PlayerActionMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        buffer.writeByte(message.getAction());
        ByteBufUtils.writeVarInt(buffer, message.getJumpBoost());
        return buffer;
    }
}
