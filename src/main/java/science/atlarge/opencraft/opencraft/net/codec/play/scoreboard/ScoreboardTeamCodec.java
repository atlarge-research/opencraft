package science.atlarge.opencraft.opencraft.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import science.atlarge.opencraft.opencraft.net.message.play.scoreboard.ScoreboardTeamMessage;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public final class ScoreboardTeamCodec implements Codec<ScoreboardTeamMessage> {

    @Override
    public ScoreboardTeamMessage decode(ByteBuf buffer) throws IOException {

        String teamName = ByteBufUtils.readUTF8(buffer);

        byte actionIndex = buffer.readByte();
        ScoreboardTeamMessage.Action action = ScoreboardTeamMessage.Action.values()[actionIndex];

        String displayName = null;
        String prefix = null;
        String suffix = null;
        boolean friendlyFire = false;
        boolean seeInvisible = false;
        Team.OptionStatus nameTagVisibility = Team.OptionStatus.NEVER;
        Team.OptionStatus collisionRule = Team.OptionStatus.NEVER;
        ChatColor color = ChatColor.RESET;
        List<String> players = null;

        if (action == ScoreboardTeamMessage.Action.CREATE || action == ScoreboardTeamMessage.Action.UPDATE) {

            displayName = ByteBufUtils.readUTF8(buffer);
            prefix = ByteBufUtils.readUTF8(buffer);
            suffix = ByteBufUtils.readUTF8(buffer);

            byte flags = buffer.readByte();
            friendlyFire = (flags & 1) > 0;
            seeInvisible = (flags & 2) > 0;

            String nameTagVisibilityName = ByteBufUtils.readUTF8(buffer).toUpperCase();
            nameTagVisibility = Team.OptionStatus.valueOf(nameTagVisibilityName);

            String collisionRuleName = ByteBufUtils.readUTF8(buffer).toUpperCase();
            collisionRule = Team.OptionStatus.valueOf(collisionRuleName);

            byte colorIndex = buffer.readByte();
            color = ChatColor.RESET;
            if (colorIndex >= 0) {
                color = ChatColor.values()[colorIndex];
            }
        }

        if (action == ScoreboardTeamMessage.Action.CREATE || action == ScoreboardTeamMessage.Action.ADD_PLAYERS || action == ScoreboardTeamMessage.Action.REMOVE_PLAYERS) {
            int playerCount = ByteBufUtils.readVarInt(buffer);
            players = new ArrayList<>(playerCount);
            for (int playerIndex = 0; playerIndex < playerCount; playerIndex++) {
                String player = ByteBufUtils.readUTF8(buffer);
                players.add(player);
            }
        }

        switch (action) {

            case CREATE:
                return ScoreboardTeamMessage.create(
                        teamName,
                        displayName,
                        prefix,
                        suffix,
                        friendlyFire,
                        seeInvisible,
                        nameTagVisibility,
                        collisionRule,
                        color,
                        players
                );

            case UPDATE:
                return ScoreboardTeamMessage.update(
                        teamName,
                        displayName,
                        prefix,
                        suffix,
                        friendlyFire,
                        seeInvisible,
                        nameTagVisibility,
                        collisionRule,
                        color
                );

            case ADD_PLAYERS:
                return ScoreboardTeamMessage.addPlayers(teamName, players);

            case REMOVE_PLAYERS:
                return ScoreboardTeamMessage.removePlayers(teamName, players);

            case REMOVE:
                return ScoreboardTeamMessage.remove(teamName);

            default:
                throw new DecoderException("Unsupported action: " + action);
        }
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, ScoreboardTeamMessage message) throws IOException {

        ByteBufUtils.writeUTF8(buffer, message.getTeamName());

        ScoreboardTeamMessage.Action action = message.getAction();
        buffer.writeByte(action.ordinal());

        // CREATE and UPDATE
        if (action == ScoreboardTeamMessage.Action.CREATE || action == ScoreboardTeamMessage.Action.UPDATE) {
            ByteBufUtils.writeUTF8(buffer, message.getDisplayName());
            ByteBufUtils.writeUTF8(buffer, message.getPrefix());
            ByteBufUtils.writeUTF8(buffer, message.getSuffix());
            buffer.writeByte(message.getFlags());
            ByteBufUtils.writeUTF8(buffer, message.getNametagVisibility().name().toLowerCase());
            ByteBufUtils.writeUTF8(buffer, message.getCollisionRule().name().toLowerCase());
            buffer.writeByte(message.getColor() == ChatColor.RESET ? -1 : message.getColor().ordinal());
        }

        // CREATE, ADD_, and REMOVE_PLAYERS
        if (action == ScoreboardTeamMessage.Action.CREATE || action == ScoreboardTeamMessage.Action.ADD_PLAYERS || action == ScoreboardTeamMessage.Action.REMOVE_PLAYERS) {
            List<String> players = message.getEntries();
            ByteBufUtils.writeVarInt(buffer, players.size());
            for (String entry : players) {
                ByteBufUtils.writeUTF8(buffer, entry);
            }
        }

        return buffer;
    }
}
