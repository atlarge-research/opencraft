package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.game.JoinGameMessage;

public final class JoinGameCodec implements Codec<JoinGameMessage> {

    @Override
    public JoinGameMessage decode(ByteBuf buffer) throws IOException {
        int id = buffer.readInt();
        byte gameMode = buffer.readByte();
        int dimension = buffer.readInt();
        byte difficulty = buffer.readByte();
        byte maxPlayers = buffer.readByte();
        String levelType = ByteBufUtils.readUTF8(buffer);
        boolean reducedDebug = buffer.readBoolean();
        return new JoinGameMessage(id, gameMode, dimension, difficulty, maxPlayers, levelType,
            reducedDebug);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, JoinGameMessage message) throws IOException {
        buffer.writeInt(message.getId());
        buffer.writeByte(message.getMode());
        buffer.writeInt(message.getDimension());
        buffer.writeByte(message.getDifficulty());
        buffer.writeByte(message.getMaxPlayers());
        ByteBufUtils.writeUTF8(buffer, message.getLevelType());
        buffer.writeBoolean(message.isReducedDebugInfo());
        return buffer;
    }
}
