package science.atlarge.opencraft.opencraft.net.message.play.player;

import com.flowpowered.network.Message;
import lombok.Data;

@Data
public class TeleportConfirmMessage implements Message {

    private final int teleportId;
}
