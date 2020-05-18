package net.glowstone.messaging;

import com.google.common.eventbus.EventBus;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

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
        EventBus channel = channels.computeIfAbsent(topic, t -> new EventBus());
        channel.unregister(listener);
    }

    @Override
    public void publish(Topic topic, Message message) {
        EventBus channel = new EventBus();
        channels.computeIfAbsent(topic, t -> channel);
        channel.post(message);
    }
}
