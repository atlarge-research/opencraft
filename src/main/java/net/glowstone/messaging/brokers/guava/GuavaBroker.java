package net.glowstone.messaging.brokers.guava;

import com.google.common.eventbus.EventBus;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.glowstone.messaging.Broker;

public class GuavaBroker<Topic, Subscriber, Message> implements Broker<Topic, Subscriber, Message> {

    private final Map<Topic, EventBus> channels;

    public GuavaBroker() {
        channels = new ConcurrentHashMap<>();
    }

    @Override
    public void subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback) {
        GuavaListener<Subscriber, Message> listener = new GuavaListener<>(subscriber, callback);
        EventBus channel = channels.computeIfAbsent(topic, t -> new EventBus());
        channel.register(listener);
    }

    @Override
    public void unsubscribe(Topic topic, Subscriber subscriber) {
        GuavaListener<Subscriber, Message> listener = new GuavaListener<>(subscriber, null);
        EventBus channel = channels.getOrDefault(topic, new EventBus());
        try {
            channel.unregister(listener);

        } catch (IllegalArgumentException e) {
            //
        }
    }

    @Override
    public void publish(Topic topic, Message message) {
        EventBus channel = channels.getOrDefault(topic, new EventBus()); // Look if thread safe
        channel.post(message);
    }
}
