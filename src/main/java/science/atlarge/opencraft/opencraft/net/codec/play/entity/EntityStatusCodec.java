package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityStatusMessage;

public final class EntityStatusCodec implements Codec<EntityStatusMessage> {

    @Override
    public EntityStatusMessage decode(ByteBuf buffer) {
        int id = buffer.readInt();
        int status = buffer.readByte();
        return new EntityStatusMessage(id, status);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, EntityStatusMessage message) {
        buffer.writeInt(message.getId());
        buffer.writeByte(message.getStatus());
        return buffer;
    }
}
