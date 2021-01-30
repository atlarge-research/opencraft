package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.entity.VehicleMoveMessage;

public class VehicleMoveCodec implements Codec<VehicleMoveMessage> {

    @Override
    public VehicleMoveMessage decode(ByteBuf buffer) throws IOException {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        float yaw = buffer.readFloat();
        float pitch = buffer.readFloat();
        return new VehicleMoveMessage(x, y, z, yaw, pitch);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, VehicleMoveMessage message) throws IOException {
        buffer.writeDouble(message.getX());
        buffer.writeDouble(message.getY());
        buffer.writeDouble(message.getZ());
        buffer.writeFloat(message.getYaw());
        buffer.writeFloat(message.getPitch());
        return buffer;
    }
}
