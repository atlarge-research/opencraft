package science.atlarge.opencraft.opencraft.messaging;

import com.flowpowered.network.Message;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import science.atlarge.opencraft.messaging.MessagingSystem;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;

public class PubSubMessaging implements Messaging {

    private final MessagingSystem<Chunk, Object, Player, Message> messageMessagingSystem;
    /**
     * Static because PubSubMessaging system is created per world (world, world_nether, world_end).
     * We are interested in the total number of packets.
     */
    private static final LongAdder adder = new LongAdder();
    private static final Map<GlowPlayer, Consumer<Message>> callbackMap = new ConcurrentHashMap<>();

    public PubSubMessaging(MessagingSystem<Chunk, Object, Player, Message> messageMessagingSystem) {
        this.messageMessagingSystem = messageMessagingSystem;
    }

    @Override
    public void update(GlowPlayer sub) {
        this.messageMessagingSystem.update(sub, callbackMap.computeIfAbsent(sub, glowPlayer -> msg -> {
            adder.increment();
            glowPlayer.getSession().send(msg);
        }));
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

    @Override
    public long totalMessagesSent() {
        return adder.sum();
    }
}
