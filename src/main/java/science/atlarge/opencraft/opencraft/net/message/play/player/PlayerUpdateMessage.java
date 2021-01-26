package science.atlarge.opencraft.opencraft.net.message.play.player;

import com.flowpowered.network.Message;
import lombok.Data;
import org.bukkit.Location;

/**
 * Base class for player update messages.
 */
@Data
public class PlayerUpdateMessage implements Message {

    private final boolean onGround;

    // TODO make this method abstract
    public void update(Location location) {
        // do nothing
    }

    public boolean moved() {
        return false;
    }
}
