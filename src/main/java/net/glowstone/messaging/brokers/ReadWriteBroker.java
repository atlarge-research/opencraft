package net.glowstone.messaging.brokers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import net.glowstone.messaging.Channel;

/**
 * The read-write broker uses a hash map to store topic-channel pairs and a read-write lock to guarantee thread-safety.
 * The lock allows multiple publishers to publish messages simultaneously, while preventing subscribers from changing
 * their subscription status at the same time.
 *
 * @param <Topic> the type of topics that is allowed to identify channels.
 * @param <Subscriber> the type of subscribers that is allowed to subscribe to a channel.
 * @param <Message> the type of messages that is allowed to be published to a channel.
 */
public final class ReadWriteBroker<Topic, Subscriber, Message> extends ChannelBroker<Topic, Subscriber, Message> {

    private final Map<Topic, Channel<Subscriber, Message>> channels;
    private final Lock readLock;
    private final Lock writeLock;

    /**
     * Create a read-write broker.
     */
    public ReadWriteBroker(ChannelFactory<Subscriber, Message> channelFactory) {
        super(channelFactory);
        channels = new HashMap<>();
        ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    @Override
    public void subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback) {
        writeLock.lock();
        try {
            Channel<Subscriber, Message> channel = channels.computeIfAbsent(topic, t -> createChannel());
            channel.subscribe(subscriber, callback);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void unsubscribe(Topic topic, Subscriber subscriber) {
        writeLock.lock();
        try {
            Channel<Subscriber, Message> channel = channels.get(topic);
            if (channel != null) {
                channel.unsubscribe(subscriber);
                if (channel.isEmpty()) {
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
            Channel<Subscriber, Message> channel = channels.get(topic);
            if (channel != null) {
                channel.publish(message);
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}
