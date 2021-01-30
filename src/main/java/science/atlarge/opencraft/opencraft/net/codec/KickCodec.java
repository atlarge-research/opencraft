package science.atlarge.opencraft.opencraft.net.codec;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.KickMessage;
import science.atlarge.opencraft.opencraft.util.TextMessage;

public final class KickCodec implements Codec<KickMessage> {

    @Override
    public KickMessage decode(ByteBuf buffer) throws IOException {
        TextMessage value = GlowBufUtils.readChat(buffer);
        return new KickMessage(value);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, KickMessage message) throws IOException {
        GlowBufUtils.writeChat(buffer, message.getText());
        return buffer;
    }
}
