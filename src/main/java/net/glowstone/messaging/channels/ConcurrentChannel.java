package net.glowstone.messaging.channels;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.glowstone.messaging.Channel;

/**
 * The concurrent channel uses a concurrent hash map to store channel-callback pairs. The concurrent hash map allows
 * multiple publishers and subscribers to access the channel simultaneously.
 *
 * @param <Subscriber> the type of subscribers that is allowed to subscribe to the channel.
 * @param <Message> the type of messages that is allowed to be published to the channel.
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
    public void subscribe(Subscriber subscriber, Consumer<Message> callback) {
        callbacks.putIfAbsent(subscriber, callback);
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
