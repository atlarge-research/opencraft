package science.atlarge.opencraft.opencraft.net.message.play.player;

import com.flowpowered.network.Message;
import lombok.Data;

@Data
public final class ResourcePackStatusMessage implements Message {

    private final int result;

}

