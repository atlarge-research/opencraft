package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.player.ClientStatusMessage;

public final class ClientStatusCodec implements Codec<ClientStatusMessage> {

    @Override
    public ClientStatusMessage decode(ByteBuf buffer) {
        int action = buffer.readUnsignedByte();
        return new ClientStatusMessage(action);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ClientStatusMessage message) {
        buffer.writeByte(message.getAction());
        return buffer;
    }
}
