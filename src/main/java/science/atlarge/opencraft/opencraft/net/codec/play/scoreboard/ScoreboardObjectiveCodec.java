package science.atlarge.opencraft.opencraft.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.scoreboard.ScoreboardObjectiveMessage;
import science.atlarge.opencraft.opencraft.scoreboard.RenderType;

public final class ScoreboardObjectiveCodec implements Codec<ScoreboardObjectiveMessage> {

    @Override
    public ScoreboardObjectiveMessage decode(ByteBuf buffer) throws IOException {
        String name = ByteBufUtils.readUTF8(buffer);
        byte action = buffer.readByte();
        switch (action) {

            // CREATE
            case 0:
                String displayName = ByteBufUtils.readUTF8(buffer);
                return ScoreboardObjectiveMessage.create(name, displayName);

            // REMOVE
            case 1:
                return ScoreboardObjectiveMessage.remove(name);

            // UPDATE
            case 2:
                String updatedDisplayName = ByteBufUtils.readUTF8(buffer);
                String renderTypeName = ByteBufUtils.readUTF8(buffer);
                RenderType renderType = RenderType.valueOf(renderTypeName);
                return ScoreboardObjectiveMessage.update(name, updatedDisplayName, renderType);

            default:
                throw new DecoderException("Unsupported action: " + action);
        }
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
