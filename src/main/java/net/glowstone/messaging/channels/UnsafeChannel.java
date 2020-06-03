package net.glowstone.messaging.channels;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.glowstone.messaging.Channel;

/**
 * The unsafe channel uses a hashmap to store subscriber-callback pairs and does not guarantee any thread-safety.
 *
 * @param <Subscriber> the type of subscribers that is allowed to subscribe to the channel.
 * @param <Message> the type of messages that is allowed to be published to the channel.
 */
public final class UnsafeChannel<Subscriber, Message> implements Channel<Subscriber, Message> {

    private final Map<Subscriber, Consumer<Message>> callbacks;

    /**
     * Create an unsafe channel.
     */
    public UnsafeChannel() {
        this.callbacks = new HashMap<>();
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
        callbacks.forEach((subscriber, callback) -> {
            callback.accept(message);
        });
    }
}
