package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.game.RespawnMessage;

public final class RespawnCodec implements Codec<RespawnMessage> {

    @Override
    public RespawnMessage decode(ByteBuf buffer) throws IOException {
        int dimension = buffer.readInt();
        int difficulty = buffer.readByte();
        int mode = buffer.readByte();
        String levelType = ByteBufUtils.readUTF8(buffer);
        return new RespawnMessage(dimension, difficulty, mode, levelType);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, RespawnMessage message) throws IOException {
        buffer.writeInt(message.getDimension());
        buffer.writeByte(message.getDifficulty());
        buffer.writeByte(message.getMode());
        ByteBufUtils.writeUTF8(buffer, message.getLevelType());
        return buffer;
    }
}
