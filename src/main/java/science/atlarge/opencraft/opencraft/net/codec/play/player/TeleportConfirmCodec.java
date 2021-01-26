package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.player.TeleportConfirmMessage;

public class TeleportConfirmCodec implements Codec<TeleportConfirmMessage> {

    @Override
    public TeleportConfirmMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        return new TeleportConfirmMessage(id);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, TeleportConfirmMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getTeleportId());
        return buffer;
    }
}
