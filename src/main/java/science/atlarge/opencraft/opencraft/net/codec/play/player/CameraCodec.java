package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.player.CameraMessage;

public final class CameraCodec implements Codec<CameraMessage> {

    @Override
    public CameraMessage decode(ByteBuf buffer) throws IOException {
        int cameraId = ByteBufUtils.readVarInt(buffer);
        return new CameraMessage(cameraId);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, CameraMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getCameraId());
        return buffer;
    }
}
