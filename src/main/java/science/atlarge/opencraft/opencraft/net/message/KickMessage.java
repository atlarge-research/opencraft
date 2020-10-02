package science.atlarge.opencraft.opencraft.net.message;

import com.flowpowered.network.Message;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import science.atlarge.opencraft.opencraft.util.TextMessage;

@Data
@RequiredArgsConstructor
public final class KickMessage implements Message {

    private final TextMessage text;

    public KickMessage(String text) {
        this(new TextMessage(text));
    }

}
