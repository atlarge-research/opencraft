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
 * @param <Message> the type of messages that a guava channel allows.
 */
@SuppressWarnings("UnstableApiUsage")
public final class GuavaChannel<Subscriber, Message> implements Channel<Subscriber, Message> {

    private final EventBus eventBus;
    private final Map<Subscriber, GuavaListener<Message>> listeners;

    /**
     * Constructor for a guava channel.
     */
    public GuavaChannel() {
        eventBus = new EventBus();
        listeners = new ConcurrentHashMap<>();
    }

    /**
     * Checks if the channel is empty.
     *
     * @return whether the channel is empty.
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
        GuavaListener<Message> listener = listeners.remove(subscriber);
        if (listener != null) {
            try {
                eventBus.unregister(listener);
            } catch (IllegalArgumentException ignored) {
                // We don't need to do anything if we want to unsubscribe a subscriber that is not subscribed
            }
        }
    }

    @Override
    public void publish(Message message) {
        eventBus.post(message);
    }
}
