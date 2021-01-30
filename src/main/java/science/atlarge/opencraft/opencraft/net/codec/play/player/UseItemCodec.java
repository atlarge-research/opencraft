package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.player.UseItemMessage;

public class UseItemCodec implements Codec<UseItemMessage> {

    @Override
    public UseItemMessage decode(ByteBuf buffer) throws IOException {
        int hand = ByteBufUtils.readVarInt(buffer);
        return new UseItemMessage(hand);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, UseItemMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getHand());
        return buffer;
    }
}
