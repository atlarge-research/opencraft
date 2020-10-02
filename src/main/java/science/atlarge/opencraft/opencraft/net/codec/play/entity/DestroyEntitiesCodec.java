package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import science.atlarge.opencraft.opencraft.net.message.play.entity.DestroyEntitiesMessage;

public final class DestroyEntitiesCodec implements Codec<DestroyEntitiesMessage> {

    @Override
    public DestroyEntitiesMessage decode(ByteBuf buffer) throws IOException {
        int size = ByteBufUtils.readVarInt(buffer);
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ids.add(ByteBufUtils.readVarInt(buffer));
        }
        return new DestroyEntitiesMessage(ids);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, DestroyEntitiesMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getIds().size());
        for (int id : message.getIds()) {
            ByteBufUtils.writeVarInt(buffer, id);
        }
        return buffer;
    }
}
