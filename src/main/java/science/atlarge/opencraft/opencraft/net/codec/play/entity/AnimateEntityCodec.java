package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.entity.AnimateEntityMessage;

public final class AnimateEntityCodec implements Codec<AnimateEntityMessage> {

    @Override
    public AnimateEntityMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        int animation = buffer.readUnsignedByte();
        return new AnimateEntityMessage(id, animation);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, AnimateEntityMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        buffer.writeByte(message.getAnimation());
        return buffer;
    }
}
