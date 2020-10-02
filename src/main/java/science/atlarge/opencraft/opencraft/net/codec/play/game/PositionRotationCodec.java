package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.game.PositionRotationMessage;

public final class PositionRotationCodec implements Codec<PositionRotationMessage> {

    @Override
    public PositionRotationMessage decode(ByteBuf buffer) throws IOException {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        float rotation = buffer.readFloat();
        float pitch = buffer.readFloat();
        int flags = buffer.readUnsignedByte();
        int teleportId = ByteBufUtils.readVarInt(buffer);
        return new PositionRotationMessage(x, y, z, rotation, pitch, flags, teleportId);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, PositionRotationMessage message) {
        buffer.writeDouble(message.getX());
        buffer.writeDouble(message.getY());
        buffer.writeDouble(message.getZ());
        buffer.writeFloat(message.getRotation());
        buffer.writeFloat(message.getPitch());
        buffer.writeByte(message.getFlags());
        ByteBufUtils.writeVarInt(buffer, message.getTeleportId());
        return buffer;
    }
}
