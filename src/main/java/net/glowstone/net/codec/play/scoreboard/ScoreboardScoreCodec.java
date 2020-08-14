package net.glowstone.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.scoreboard.ScoreboardScoreMessage;

public final class ScoreboardScoreCodec implements Codec<ScoreboardScoreMessage> {

    @Override
    public ScoreboardScoreMessage decode(ByteBuf buffer) throws IOException {
        String target = ByteBufUtils.readUTF8(buffer);
        boolean remove = buffer.readByte() != 0;
        String objective = ByteBufUtils.readUTF8(buffer);
        if (!remove) {
            int value = ByteBufUtils.readVarInt(buffer);
            return new ScoreboardScoreMessage(target, objective, value);
        }
        return ScoreboardScoreMessage.remove(target, objective);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ScoreboardScoreMessage message) throws IOException {
        boolean remove = message.isRemove();
        ByteBufUtils.writeUTF8(buffer, message.getTarget());
        buffer.writeByte(remove ? 1 : 0);
        ByteBufUtils.writeUTF8(buffer, message.getObjective());
        if (!remove) {
            ByteBufUtils.writeVarInt(buffer, message.getValue());
        }
        return buffer;
    }
}
