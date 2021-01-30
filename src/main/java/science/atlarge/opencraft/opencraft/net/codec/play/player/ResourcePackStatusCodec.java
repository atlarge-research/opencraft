package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.player.ResourcePackStatusMessage;

public final class ResourcePackStatusCodec implements Codec<ResourcePackStatusMessage> {

    @Override
    public ResourcePackStatusMessage decode(ByteBuf buffer) throws IOException {
        int result = ByteBufUtils.readVarInt(buffer);
        return new ResourcePackStatusMessage(result);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ResourcePackStatusMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getResult());
        return buffer;
    }
}
