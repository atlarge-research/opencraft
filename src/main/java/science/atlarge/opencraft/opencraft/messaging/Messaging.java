package science.atlarge.opencraft.opencraft.messaging;

import com.flowpowered.network.Message;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;

public interface Messaging {
    void update(GlowPlayer sub);

    void remove(GlowPlayer sub);

    void publish(Object sub, Message message);

    void close();

    long totalMessagesSent();
}
