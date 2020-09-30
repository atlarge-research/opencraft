package net.glowstone.messaging;

import com.flowpowered.network.Message;
import java.util.function.Consumer;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import science.atlarge.opencraft.messaging.MessagingSystem;

public class PubSubMessaging implements Messaging {

    private final MessagingSystem<Chunk, Object, Player, Message> messageMessagingSystem;

    public PubSubMessaging(MessagingSystem<Chunk, Object, Player, Message> messageMessagingSystem) {
        this.messageMessagingSystem = messageMessagingSystem;
    }

    @Override
    public void update(GlowPlayer sub, Consumer<Message> callback) {
        this.messageMessagingSystem.update(sub, callback);
    }

    @Override
    public void remove(GlowPlayer sub) {
        // Nothing to do here.
    }

    @Override
    public void publish(Object sub, Message message) {
        this.messageMessagingSystem.broadcast(sub, message);
    }

    @Override
    public void close() {
        this.messageMessagingSystem.close();
    }
}
