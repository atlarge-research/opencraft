package science.atlarge.opencraft.opencraft.net.message.play.game;

import com.flowpowered.network.Message;
import lombok.Data;
import science.atlarge.opencraft.opencraft.util.TextMessage;

@Data
public final class UserListHeaderFooterMessage implements Message {

    private final TextMessage header;
    private final TextMessage footer;

}
