package net.glowstone.messaging.channels;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import net.glowstone.messaging.Channel;

/**
 * The read-write channel uses a hashmap to store subscriber-callback pairs and uses a read-write lock to guarantee
 * thread-safety. It allows only a single subscriber to subscribe or unsubscribe at a time. However, many publishers may
 * publish messages to the subscribers simultaneously.
 *
 * @param <Subscriber> the type of subscribers that is allowed to subscribe to the channel.
 * @param <Message> the type of messages that is allowed to be published to the channel.
 */
public final class ReadWriteChannel<Subscriber, Message> implements Channel<Subscriber, Message> {

    private final Map<Subscriber, Consumer<Message>> callbacks;
    private final Lock readLock;
    private final Lock writeLock;

    /**
     * Create a read-write channel.
     */
    public ReadWriteChannel() {
        this.callbacks = new HashMap<>();
        ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    @Override
    public boolean isEmpty() {
        readLock.lock();
        try {
            return callbacks.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void subscribe(Subscriber subscriber, Consumer<Message> callback) {
        writeLock.lock();
        try {
            callbacks.putIfAbsent(subscriber, callback);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        writeLock.lock();
        try {
            callbacks.remove(subscriber);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void publish(Message message) {
        readLock.lock();
        try {
            callbacks.forEach((subscriber, callback) -> {
                callback.accept(message);
            });
        } finally {
            readLock.unlock();
        }
    }
}
