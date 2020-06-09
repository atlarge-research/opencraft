package net.glowstone.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import java.io.IOException;
import net.glowstone.net.message.play.scoreboard.ScoreboardObjectiveMessage;

public final class ScoreboardObjectiveCodec implements Codec<ScoreboardObjectiveMessage> {

    @Override
    public ScoreboardObjectiveMessage decode(ByteBuf buffer) {
        throw new DecoderException("Cannot decode ScoreboardObjectiveMessage");
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ScoreboardObjectiveMessage message) throws IOException {
        ByteBufUtils.writeUTF8(buffer, message.getName());
        buffer.writeByte(message.getAction());
        if (message.getDisplayName() != null) {
            ByteBufUtils.writeUTF8(buffer, message.getDisplayName());
        }
        if (message.getRenderType() != null) {
            ByteBufUtils.writeUTF8(buffer, message.getRenderType().name());
        }
        return buffer;
    }
}
