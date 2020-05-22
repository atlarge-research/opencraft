package net.glowstone.messaging.brokers.guava;

import com.google.common.eventbus.EventBus;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import net.glowstone.messaging.Broker;

/**
 * The guava broker uses a concurrent hash map to store topic-eventBus pairs. The concurrent
 * hash map allows multiple publishers and subscribers to access the broker simultaneously.
 * The eventBus makes use of an unstable class.
 *
 * @param <Topic> the type of topics that is allowed to identify channels.
 * @param <Subscriber> the type of subscribers that is allowed to subscribe to a channel.
 * @param <Message> the type of messages that is allowed to be published to a channel.
 */
@SuppressWarnings("UnstableApiUsage")
public class GuavaBroker<Topic, Subscriber, Message> implements Broker<Topic, Subscriber, Message> {

    private final Map<Topic, EventBus> channels;
    private final Map<Topic, Map<Subscriber, GuavaListener<Message>>> subscribers;

    private final Lock writeLock;
    private final Lock readLock;

    /**
     * Constructing a new guava broker.
     */
    public GuavaBroker() {

        channels = new HashMap<>();
        subscribers = new HashMap<>();

        ReadWriteLock lock = new ReentrantReadWriteLock();
        writeLock = lock.writeLock();
        readLock = lock.readLock();
    }

    @Override
    public void subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback) {

        writeLock.lock();
        try {

            GuavaListener<Message> listener = new GuavaListener<>(callback);

            Map<Subscriber, GuavaListener<Message>> listeners = subscribers.computeIfAbsent(
                topic,
                t -> new HashMap<>()
            );
            EventBus eventBus;
            if (listeners.isEmpty()) {
                eventBus = new EventBus();
                channels.put(topic, eventBus);
            } else {
                eventBus = channels.get(topic);
            }
            listeners.put(subscriber, listener);
            eventBus.register(listener);

        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void unsubscribe(Topic topic, Subscriber subscriber) {

        writeLock.lock();
        try {

            Map<Subscriber, GuavaListener<Message>> listeners = subscribers.computeIfAbsent(
                topic,
                t -> new HashMap<>()
            );
            GuavaListener<Message> listener = listeners.get(subscriber);
            EventBus eventBus = channels.get(topic);
            if (listener != null) {
                try {
                    eventBus.unregister(listener);

                } catch (IllegalArgumentException ignored) {
                    // Don't need to do anything when a listener is not used.
                }
                listeners.remove(subscriber);

                if (listeners.isEmpty()) {
                    subscribers.remove(topic);
                    channels.remove(topic);
                }
            }

        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void publish(Topic topic, Message message) {
        readLock.lock();
        try {
            EventBus channel = channels.computeIfAbsent(topic, t -> new EventBus());
            channel.post(message);

        } finally {
            readLock.unlock();
        }
    }
}
