package net.glowstone.messaging.brokers.concurrent;

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

    private final Map<Topic, ConcurrentChannel<Subscriber, Message>> channels;

    /**
     * Create a concurrent broker.
     */
    public ConcurrentBroker() {
        channels = new ConcurrentHashMap<>();
    }

    /**
     * Check whether the broker is empty, meaning that there are no subscribers.
     *
     * @return whether there are any subscribers.
     */
    boolean isEmpty() {
        return channels.isEmpty();
    }

    /**
     * Check whether a subscriber is subscribed to the topic.
     *
     * @param topic the topic of interest.
     * @param subscriber the subscriber whom's subscription should be checked.
     * @return whether the subscriber is subscribed to the topic.
     */
    boolean isSubscribed(Topic topic, Subscriber subscriber) {
        ConcurrentChannel<Subscriber, Message> channel = channels.get(topic);
        if (channel == null) {
            return false;
        }
        return channel.isSubscribed(subscriber);
    }

    @Override
    public void subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback) {
        channels.compute(topic, (t, channel) -> {
            if (channel == null) {
                channel = new ConcurrentChannel<>();
            }
            channel.subscribe(subscriber, callback);
            return channel;
        });
    }

    @Override
    public void unsubscribe(Topic topic, Subscriber subscriber) {
        channels.computeIfPresent(topic, (t, channel) -> {
            channel.unsubscribe(subscriber);
            if (channel.isEmpty()) {
                return null;
            }
            return channel;
        });
    }

    @Override
    public void publish(Topic topic, Message message) {
        channels.computeIfPresent(topic, (t, channel) -> {
            channel.publish(message);
            return channel;
        });
    }
}
