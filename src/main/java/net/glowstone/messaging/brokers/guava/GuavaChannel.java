package net.glowstone.messaging.brokers.guava;

import com.google.common.eventbus.EventBus;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.glowstone.messaging.Channel;

/**
 * The guava channel is used for a mapping between the subscriber and the guava listener.
 *
 * @param <Subscriber> the type of subscribers that is allowed to subscribe to a channel.
 * @param <Message> the type of messages that is allowed to be published to a channel.
 */
@SuppressWarnings("UnstableApiUsage")
public class GuavaChannel<Subscriber, Message> implements Channel<Subscriber, Message> {

    EventBus eventBus;
    private final Map<Subscriber, GuavaListener<Message>> listeners;

    /**
     * Constructor for a guava channel.
     */
    public GuavaChannel(EventBus eventBus) {
        this.eventBus = eventBus;
        this.listeners = new ConcurrentHashMap<>();
    }

    /**
     * Checks if the map is empty.
     * @return whether the map is empty.
     */
    public boolean isEmpty() {
        return listeners.isEmpty();
    }


    @Override
    public void subscribe(Subscriber subscriber, Consumer<Message> callback) {
        listeners.computeIfAbsent(subscriber, t -> {
            GuavaListener<Message> listener = new GuavaListener<>(callback);
            eventBus.register(listener);
            return listener;
        });
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        try {
            eventBus.unregister(listeners.remove(subscriber));
        } catch (IllegalArgumentException ignored) {
            // We don't need to do anything if we want to unsubscribe a subscriber that is not subscribed
        }
    }

    @Override
    public void publish(Message message) {
        if (eventBus != null) {
            eventBus.post(message);
        }
    }
}
