package science.atlarge.opencraft.opencraft.net.message;

import com.flowpowered.network.Message;
import lombok.Data;

@Data
public final class SetCompressionMessage implements Message {

    private final int threshold;

}

