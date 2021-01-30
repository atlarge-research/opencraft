package science.atlarge.opencraft.opencraft.net.message.play.inv;

import com.flowpowered.network.Message;
import lombok.Data;

@Data
public final class CloseWindowMessage implements Message {

    private final int id;

}
