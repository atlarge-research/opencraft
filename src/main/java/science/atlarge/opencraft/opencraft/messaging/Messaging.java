package science.atlarge.opencraft.opencraft.messaging;

import com.flowpowered.network.Message;
import java.util.function.Consumer;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;

public interface Messaging {
    void update(GlowPlayer sub, Consumer<Message> callback);

    void remove(GlowPlayer sub);

    void publish(Object sub, Message message);

    void close();
}
