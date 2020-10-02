package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.entity.EntityRemoveEffectMessage;

public final class EntityRemoveEffectCodec implements Codec<EntityRemoveEffectMessage> {

    @Override
    public EntityRemoveEffectMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        byte effect = buffer.readByte();
        return new EntityRemoveEffectMessage(id, effect);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, EntityRemoveEffectMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        buffer.writeByte(message.getEffect());
        return buffer;
    }
}
