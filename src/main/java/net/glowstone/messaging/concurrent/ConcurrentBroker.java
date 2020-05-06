package net.glowstone.messaging.concurrent;

import net.glowstone.messaging.Broker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class ConcurrentBroker<Topic, Subscriber, Message> implements Broker<Topic, Subscriber, Message> {

    private final Map<Topic, ConcurrentChannel<Subscriber, Message>> channels;

    public ConcurrentBroker() {
        channels = new ConcurrentHashMap<>();
    }

    @Override
    public void subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback) {
        channels.compute(topic, (t, channel)-> {
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
