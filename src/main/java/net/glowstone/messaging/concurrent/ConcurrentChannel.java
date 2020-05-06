package net.glowstone.messaging.concurrent;

import net.glowstone.messaging.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class ConcurrentChannel<Subscriber, Message> implements Channel<Subscriber, Message> {

    static private final long PARALLELISM_THRESHOLD = 4;

    private final ConcurrentHashMap<Subscriber, Consumer<Message>> callbacks;

    public ConcurrentChannel() {
        this.callbacks = new ConcurrentHashMap<>();
    }

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
