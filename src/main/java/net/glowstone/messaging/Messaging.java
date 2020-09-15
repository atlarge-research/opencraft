package net.glowstone.messaging;

import com.flowpowered.network.Message;
import java.util.function.Consumer;
import net.glowstone.entity.GlowPlayer;

public interface Messaging {
    void update(GlowPlayer sub, Consumer<Message> callback);

    void publish(Object sub, Message message);

    void close();
}
