package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityHeadRotationMessage;

public final class EntityHeadRotationCodec implements Codec<EntityHeadRotationMessage> {

    @Override
    public EntityHeadRotationMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        int rotation = buffer.readByte();
        return new EntityHeadRotationMessage(id, rotation);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, EntityHeadRotationMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        buffer.writeByte(message.getRotation());
        return buffer;
    }
}
