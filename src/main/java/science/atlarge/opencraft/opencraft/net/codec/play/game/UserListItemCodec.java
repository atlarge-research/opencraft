package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import science.atlarge.opencraft.opencraft.entity.meta.profile.GlowPlayerProfile;
import science.atlarge.opencraft.opencraft.net.GlowBufUtils;
import science.atlarge.opencraft.opencraft.net.message.play.game.UserListItemMessage;
import science.atlarge.opencraft.opencraft.util.TextMessage;

public final class UserListItemCodec implements Codec<UserListItemMessage> {

    @Override
    public UserListItemMessage decode(ByteBuf buffer) throws IOException {

        int actionIndex = ByteBufUtils.readVarInt(buffer);
        UserListItemMessage.Action action = UserListItemMessage.Action.values()[actionIndex];

        int entryCount = ByteBufUtils.readVarInt(buffer);
        List<UserListItemMessage.Entry> entries = new ArrayList<>(entryCount);
        for (int entryIndex = 0; entryIndex < entryCount; entryIndex++) {

            UUID uuid = GlowBufUtils.readUuid(buffer);

            GlowPlayerProfile profile = null;
            int gameMode = 0;
            int ping = 0;
            TextMessage displayName = null;

            switch (action) {

                case ADD_PLAYER: {
                    String profileName = ByteBufUtils.readUTF8(buffer);
                    int propertyCount = ByteBufUtils.readVarInt(buffer);
                    List<ProfileProperty> properties = new ArrayList<>(propertyCount);
                    for (int propertyIndex = 0; propertyIndex < propertyCount; propertyIndex++) {
                        String propertyName = ByteBufUtils.readUTF8(buffer);
                        String value = ByteBufUtils.readUTF8(buffer);
                        ProfileProperty property;
                        boolean propertySigned = buffer.readBoolean();
                        if (propertySigned) {
                            String signature = ByteBufUtils.readUTF8(buffer);
                            property = new ProfileProperty(propertyName, value, signature);
                        } else {
                            property = new ProfileProperty(propertyName, value);
                        }
                        properties.add(property);
                    }
                    profile = new GlowPlayerProfile(profileName, uuid, properties, false);
                    gameMode = ByteBufUtils.readVarInt(buffer);
                    ping = ByteBufUtils.readVarInt(buffer);
                    boolean named = buffer.readBoolean();
                    if (named) {
                        displayName = GlowBufUtils.readChat(buffer);
                    }
                    break;
                }

                case UPDATE_GAMEMODE:
                    gameMode = ByteBufUtils.readVarInt(buffer);
                    break;

                case UPDATE_LATENCY:
                    ping = ByteBufUtils.readVarInt(buffer);
                    break;

                case UPDATE_DISPLAY_NAME:
                    boolean named = buffer.readBoolean();
                    if (named) {
                        displayName = GlowBufUtils.readChat(buffer);
                    }
                    break;

                case REMOVE_PLAYER:
                    // Nothing
                    break;

                default:
                    throw new UnsupportedOperationException("unknown action: " + action);
            }

            UserListItemMessage.Entry entry = new UserListItemMessage.Entry(uuid, profile, gameMode, ping, displayName, action);
            entries.add(entry);
        }

        return new UserListItemMessage(action, entries);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, UserListItemMessage message) throws IOException {

        UserListItemMessage.Action action = message.getAction();
        ByteBufUtils.writeVarInt(buffer, message.getAction().ordinal());

        List<UserListItemMessage.Entry> entries = message.getEntries();
        ByteBufUtils.writeVarInt(buffer, entries.size());
        for (UserListItemMessage.Entry entry : entries) {
            GlowBufUtils.writeUuid(buffer, entry.uuid);
            // TODO: implement remaining actions
            switch (action) {
                case ADD_PLAYER:
                    ByteBufUtils.writeUTF8(buffer, entry.profile.getName());
                    ByteBufUtils.writeVarInt(buffer, entry.profile.getProperties().size());
                    for (ProfileProperty property : entry.profile.getProperties()) {
                        ByteBufUtils.writeUTF8(buffer, property.getName());
                        ByteBufUtils.writeUTF8(buffer, property.getValue());
                        buffer.writeBoolean(property.isSigned());
                        if (property.isSigned()) {
                            ByteBufUtils.writeUTF8(buffer, property.getSignature());
                        }
                    }
                    ByteBufUtils.writeVarInt(buffer, entry.gameMode);
                    ByteBufUtils.writeVarInt(buffer, entry.ping);
                    if (entry.displayName != null) {
                        buffer.writeBoolean(true);
                        GlowBufUtils.writeChat(buffer, entry.displayName);
                    } else {
                        buffer.writeBoolean(false);
                    }
                    break;

                case UPDATE_GAMEMODE:
                    ByteBufUtils.writeVarInt(buffer, entry.gameMode);
                    break;

                case UPDATE_LATENCY:
                    ByteBufUtils.writeVarInt(buffer, entry.ping);
                    break;

                case UPDATE_DISPLAY_NAME:
                    if (entry.displayName != null) {
                        buffer.writeBoolean(true);
                        GlowBufUtils.writeChat(buffer, entry.displayName);
                    } else {
                        buffer.writeBoolean(false);
                    }
                    break;

                case REMOVE_PLAYER:
                    // nothing
                    break;

                default:
                    throw new UnsupportedOperationException("not yet implemented: " + action);
            }
        }
        return buffer;
    }
}
