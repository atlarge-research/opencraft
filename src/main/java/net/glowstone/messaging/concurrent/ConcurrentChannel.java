package net.glowstone.messaging.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.glowstone.messaging.Channel;

/**
 * The concurrent broker uses a concurrent hashmap to store topic-channel pairs. The concurrent
 * hash map allows multiple publishers and subscribers to access to broker simultaneously.
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

    /**
     * Check whether the channel is empty.
     *
     * @return whether or not their are any subscribers to this channel.
     */
    public boolean isEmpty() {
        return callbacks.isEmpty();
    }

    @Override
    public void subscribe(Subscriber subscriber, Consumer<Message> callback) {
        callbacks.put(subscriber, callback);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        callbacks.remove(subscriber);
    }

    @Override
    public void publish(Message message) {
        callbacks.forEachValue(PARALLELISM_THRESHOLD, callback -> callback.accept(message));
    }
}
