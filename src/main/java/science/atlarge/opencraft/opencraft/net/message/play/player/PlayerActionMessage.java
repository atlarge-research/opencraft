package science.atlarge.opencraft.opencraft.net.message.play.player;

import com.flowpowered.network.Message;
import lombok.Data;

@Data
public final class PlayerActionMessage implements Message {

    private final int id;
    private final int action;
    private final int jumpBoost;

}

