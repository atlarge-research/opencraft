package net.glowstone.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import java.io.IOException;
import java.util.List;
import net.glowstone.net.message.play.scoreboard.ScoreboardTeamMessage;
import net.glowstone.net.message.play.scoreboard.ScoreboardTeamMessage.Action;
import org.bukkit.ChatColor;

public final class ScoreboardTeamCodec implements Codec<ScoreboardTeamMessage> {

    @Override
    public ScoreboardTeamMessage decode(ByteBuf buffer) {
        throw new DecoderException("Cannot decode ScoreboardTeamMessage");
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ScoreboardTeamMessage message) throws IOException {

        ByteBufUtils.writeUTF8(buffer, message.getTeamName());

        Action action = message.getAction();
        buffer.writeByte(action.ordinal());

        // CREATE and UPDATE
        if (action == Action.CREATE || action == Action.UPDATE) {
            ByteBufUtils.writeUTF8(buffer, message.getDisplayName());
            ByteBufUtils.writeUTF8(buffer, message.getPrefix());
            ByteBufUtils.writeUTF8(buffer, message.getSuffix());
            buffer.writeByte(message.getFlags());
            ByteBufUtils.writeUTF8(buffer, message.getNametagVisibility().name().toLowerCase());
            ByteBufUtils.writeUTF8(buffer, message.getCollisionRule().name().toLowerCase());
            buffer.writeByte(
                message.getColor() == ChatColor.RESET ? -1 : message.getColor().ordinal());
        }

        // CREATE, ADD_, and REMOVE_PLAYERS
        if (action == Action.CREATE || action == Action.ADD_PLAYERS
            || action == Action.REMOVE_PLAYERS) {
            List<String> entries = message.getEntries();
            ByteBufUtils.writeVarInt(buffer, entries.size());
            for (String entry : entries) {
                ByteBufUtils.writeUTF8(buffer, entry);
            }
        }

        return buffer;
    }
}
