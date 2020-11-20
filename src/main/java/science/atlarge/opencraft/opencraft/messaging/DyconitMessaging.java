package science.atlarge.opencraft.opencraft.messaging;

import com.flowpowered.network.Message;
import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import science.atlarge.opencraft.dyconits.Dyconit;
import science.atlarge.opencraft.dyconits.DyconitSystem;
import science.atlarge.opencraft.dyconits.MessageChannel;
import science.atlarge.opencraft.dyconits.Subscriber;
import science.atlarge.opencraft.dyconits.policies.DyconitPolicy;
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;

public class DyconitMessaging implements Messaging {

    private final DyconitSystem<Player, Message> system;
    private final Map<GlowPlayer, Subscriber<Player, Message>> subscriberMap = new HashMap<>();

    private final LongAdder adder = new LongAdder();

    public DyconitMessaging(DyconitSystem<Player, Message> system) {
        this.system = system;
        logPolicy();
    }

    @Override
    public void update(GlowPlayer sub) {
        if (!sub.isDisconnected()) {
            Subscriber<Player, Message> subscriber = subscriberMap
                    .computeIfAbsent(
                            sub,
                            s -> new Subscriber<>(s, new DyconitMessageChannel(s.getSession().getChannel(), adder)));
            system.update(subscriber);
        } else {
            remove(sub);
        }
    }

    @Override
    public void remove(GlowPlayer sub) {
        system.unsubscribeAll(sub);
        subscriberMap.remove(sub);
    }

    @Override
    public void publish(Object sub, Message message) {
        system.publish(sub, message);
    }

    @Override
    public void close() {
        // Nothing to close.
    }

    public DyconitPolicy<Player, Message> getPolicy() {
        return system.getPolicy();
    }

    public void setPolicy(DyconitPolicy<Player, Message> policy) {
        system.setPolicy(policy);
        logPolicy();
    }

    private void logPolicy() {
        GlowServer.logger.info("Dyconit System using policy: " + system.getPolicy().getClass().getSimpleName());
    }

    @Override
    public long totalMessagesSent() {
        return adder.sum();
    }

    public List<String> getDyconits() {
        return system.getDyconits().stream().map(Dyconit::getName).collect(Collectors.toList());
    }

    private static class DyconitMessageChannel implements MessageChannel<Message> {
        private final Channel channel;
        private final LongAdder adder;

        public DyconitMessageChannel(Channel channel, LongAdder adder) {
            this.channel = channel;
            this.adder = adder;
        }

        @Override
        public void flush() {
            channel.flush();
        }

        @Override
        public void send(Message message) {
            adder.increment();
            channel.write(message);
        }
    }
}
