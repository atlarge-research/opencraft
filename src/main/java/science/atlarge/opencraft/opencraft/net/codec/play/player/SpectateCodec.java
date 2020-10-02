package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.player.SpectateMessage;

public final class SpectateCodec implements Codec<SpectateMessage> {

    @Override
    public SpectateMessage decode(ByteBuf buffer) {
        UUID uuid = GlowBufUtils.readUuid(buffer);
        return new SpectateMessage(uuid);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SpectateMessage message) {
        GlowBufUtils.writeUuid(buffer, message.getTarget());
        return buffer;
    }
}
