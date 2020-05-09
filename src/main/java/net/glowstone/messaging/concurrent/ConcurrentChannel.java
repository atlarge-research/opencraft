package net.glowstone.messaging.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.glowstone.messaging.Channel;

/**
 * The concurrent broker uses a concurrent hashmap to store topic-channel pairs. The concurrent
 * hash map allows multiple publishers and subscribers to access the broker simultaneously.
 *
 * @param <Subscriber> the type of subscribers that is allowed to subscribe to a channel.
 * @param <Message> the type of messages that is allowed to be published to a channel.
 */
public final class ConcurrentChannel<Subscriber, Message> implements Channel<Subscriber, Message> {

    private static final long PARALLELISM_THRESHOLD = 4;

    private final ConcurrentHashMap<Subscriber, Consumer<Message>> callbacks;

    /**
     * Create a concurrent channel.
     */
    public ConcurrentChannel() {
        this.callbacks = new ConcurrentHashMap<>();
    }

    @Override
    public boolean isEmpty() {
        return callbacks.isEmpty();
    }

    @Override
    public boolean isSubscribed(Subscriber subscriber) {
        return callbacks.containsKey(subscriber);
    }

    @Override
    public boolean subscribe(Subscriber subscriber, Consumer<Message> callback) {
        return callbacks.putIfAbsent(subscriber, callback) == null;
    }

    @Override
    public boolean unsubscribe(Subscriber subscriber) {
        return callbacks.remove(subscriber) != null;
    }

    @Override
    public void publish(Message message) {
        callbacks.forEachValue(PARALLELISM_THRESHOLD, callback -> callback.accept(message));
    }
}
