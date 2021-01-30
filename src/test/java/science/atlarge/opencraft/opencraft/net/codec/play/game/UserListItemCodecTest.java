package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import science.atlarge.opencraft.opencraft.entity.meta.profile.GlowPlayerProfile;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.UserListItemMessage;
import science.atlarge.opencraft.opencraft.util.TextMessage;

public class UserListItemCodecTest extends CodecTest<UserListItemMessage> {

    @Override
    protected Codec<UserListItemMessage> createCodec() {
        return new UserListItemCodec();
    }

    @Override
    protected UserListItemMessage createMessage() {
        List<UserListItemMessage.Entry> entries = new ArrayList<>();
        UUID uuid = UUID.randomUUID();
        GlowPlayerProfile profile = new GlowPlayerProfile("one", uuid, false);
        TextMessage displayName = new TextMessage("five");
        UserListItemMessage.Action action = UserListItemMessage.Action.ADD_PLAYER;
        entries.add(new UserListItemMessage.Entry(uuid, profile, 3, 4, displayName, action));
        return new UserListItemMessage(UserListItemMessage.Action.ADD_PLAYER, entries);
    }
}
