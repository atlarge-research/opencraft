package net.glowstone.messaging.concurrent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import net.glowstone.messaging.Broker;
import net.glowstone.messaging.Channel;

/**
 * The concurrent broker uses a concurrent hash map to store topic-channel pairs. The concurrent
 * hash map allows multiple publishers and subscribers to access the broker simultaneously.
 *
 * @param <Topic> the type of topics that is allowed to identify channels.
 * @param <Subscriber> the type of subscribers that is allowed to subscribe to a channel.
 * @param <Message> the type of messages that is allowed to be published to a channel.
 */
public final class ConcurrentBroker<Topic, Subscriber, Message> implements Broker<Topic, Subscriber, Message> {

    private final Map<Topic, Channel<Subscriber, Message>> channels;

    /**
     * Create a concurrent broker.
     */
    public ConcurrentBroker() {
        channels = new ConcurrentHashMap<>();
    }

    @Override
    public boolean isEmpty() {
        return channels.isEmpty();
    }

    @Override
    public boolean isSubscribed(Topic topic, Subscriber subscriber) {
        Channel<Subscriber, Message> channel = channels.get(topic);
        if (channel == null) {
            return false;
        }
        return channel.isSubscribed(subscriber);
    }

    @Override
    public boolean subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback) {
        AtomicBoolean subscribed = new AtomicBoolean(false);
        channels.compute(topic, (t, channel) -> {
            if (channel == null) {
                channel = new ConcurrentChannel<>();
            }
            subscribed.set(channel.subscribe(subscriber, callback));
            return channel;
        });
        return subscribed.get();
    }

    @Override
    public boolean unsubscribe(Topic topic, Subscriber subscriber) {
        AtomicBoolean unsubscribed = new AtomicBoolean(false);
        channels.computeIfPresent(topic, (t, channel) -> {
            unsubscribed.set(channel.unsubscribe(subscriber));
            if (channel.isEmpty()) {
                return null;
            }
            return channel;
        });
        return unsubscribed.get();
    }

    @Override
    public void publish(Topic topic, Message message) {
        channels.computeIfPresent(topic, (t, channel) -> {
            channel.publish(message);
            return channel;
        });
    }

    @Override
    public void close() {
        // Do nothing
    }
}
