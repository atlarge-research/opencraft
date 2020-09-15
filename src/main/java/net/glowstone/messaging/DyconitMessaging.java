package net.glowstone.messaging;

import com.flowpowered.network.Message;
import java.util.function.Consumer;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.entity.Player;
import science.atlarge.opencraft.dyconits.DyconitSystem;
import science.atlarge.opencraft.dyconits.Subscriber;

public class DyconitMessaging implements Messaging {

    private final DyconitSystem<Player, Message> system;

    public DyconitMessaging(DyconitSystem<Player, Message> system) {
        this.system = system;
    }

    @Override
    public void update(GlowPlayer sub, Consumer<Message> callback) {
        system.update(new Subscriber<>(sub, callback));
    }

    @Override
    public void publish(Object sub, Message message) {
        system.publish(sub, message);
    }

    @Override
    public void close() {
        // Nothing to close.
    }
}
