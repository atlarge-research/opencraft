package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.entity.RelativeEntityPositionMessage;

public final class RelativeEntityPositionCodec implements Codec<RelativeEntityPositionMessage> {

    @Override
    public RelativeEntityPositionMessage decode(ByteBuf buffer) throws IOException {
        int id = ByteBufUtils.readVarInt(buffer);
        short deltaX = buffer.readShort();
        short deltaY = buffer.readShort();
        short deltaZ = buffer.readShort();
        boolean onGround = buffer.readBoolean();
        return new RelativeEntityPositionMessage(id, deltaX, deltaY, deltaZ, onGround);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, RelativeEntityPositionMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getId());
        buffer.writeShort(message.getDeltaX());
        buffer.writeShort(message.getDeltaY());
        buffer.writeShort(message.getDeltaZ());
        buffer.writeBoolean(message.isOnGround());
        return buffer;
    }
}
