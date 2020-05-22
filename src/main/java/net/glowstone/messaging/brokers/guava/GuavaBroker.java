package net.glowstone.messaging.brokers.guava;

import com.google.common.eventbus.EventBus;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import net.glowstone.messaging.Broker;
import org.apache.commons.lang3.tuple.Pair;

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

    private final Map<Topic, Pair<EventBus, Integer>> channels;
    private final Map<Pair<Topic, Subscriber>, GuavaListener<Subscriber, Message>> subscribers;

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

            GuavaListener<Subscriber, Message> listener = new GuavaListener<>(subscriber, callback);

            EventBus channel;
            Pair<EventBus, Integer> eventbus;
            int size;

            if (channels.containsKey(topic)) {
                eventbus = channels.remove(topic);
                channel = eventbus.getLeft();
                size = eventbus.getRight() + 1;
                eventbus = Pair.of(channel, size);

            } else {
                channel = new EventBus();
                eventbus = Pair.of(channel, 1);
            }

            channels.put(topic, eventbus);
            channel.register(listener);

            Pair<Topic, Subscriber> pair = Pair.of(topic, subscriber);
            subscribers.put(pair, listener);

        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void unsubscribe(Topic topic, Subscriber subscriber) {

        writeLock.lock();
        try {

            Pair<Topic, Subscriber> pair = Pair.of(topic, subscriber);
            GuavaListener<Subscriber, Message> listener = subscribers.getOrDefault(pair, null);

            EventBus channel;
            Pair<EventBus, Integer> eventbus;
            int size;

            if (channels.containsKey(topic) && listener != null) {
                eventbus = channels.remove(topic);
                channel = eventbus.getLeft();
                size = eventbus.getRight() - 1;
                eventbus = Pair.of(channel, size);

                try {
                    channel.unregister(listener);

                } catch (IllegalArgumentException ignored) {
                    // Don't need to do anything when a listener is not used.
                }

                if (size > 0) {
                    channels.put(topic, eventbus);
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
            Pair<EventBus, Integer> pair = channels.getOrDefault(topic, Pair.of(new EventBus(), 0));
            EventBus channel = pair.getLeft();
            channel.post(message);

        } finally {
            readLock.unlock();
        }
    }
}
