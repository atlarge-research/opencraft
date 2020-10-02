package net.glowstone.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.scoreboard.ScoreboardDisplayMessage;

public final class ScoreboardDisplayCodec implements Codec<ScoreboardDisplayMessage> {

    @Override
    public ScoreboardDisplayMessage decode(ByteBuf buffer) throws IOException {
        byte position = buffer.readByte();
        String objective = ByteBufUtils.readUTF8(buffer);
        return new ScoreboardDisplayMessage(position, objective);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ScoreboardDisplayMessage message) throws IOException {
        buffer.writeByte(message.getPosition());
        ByteBufUtils.writeUTF8(buffer, message.getObjective());
        return buffer;
    }
}
