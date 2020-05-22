package net.glowstone.messaging.brokers.guava;

import com.google.common.eventbus.EventBus;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.glowstone.messaging.Broker;
import org.apache.commons.lang3.tuple.Pair;

public class GuavaBroker<Topic, Subscriber, Message> implements Broker<Topic, Subscriber, Message> {

    private final Map<Topic, EventBus> channels;
    private final Map<Pair<Topic, Subscriber>, GuavaListener> subscribers;

    public GuavaBroker() {
        channels = new ConcurrentHashMap<>();
        subscribers = new ConcurrentHashMap<>();
    }

    @Override
    public void subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback) {

        GuavaListener<Subscriber, Message> listener = new GuavaListener<>(subscriber, callback);
        Pair<Topic, Subscriber> pair = Pair.of(topic, subscriber);
        subscribers.put(pair, listener);

        EventBus channel = channels.computeIfAbsent(topic, t -> new EventBus());
        channel.register(listener);
    }

    @Override
    public void unsubscribe(Topic topic, Subscriber subscriber) {
        Pair<Topic, Subscriber> pair = Pair.of(topic, subscriber);
        GuavaListener<Subscriber, Message> listener = subscribers.getOrDefault(pair, null);
        EventBus channel = channels.computeIfAbsent(topic, t -> new EventBus());
        if (listener != null) {
            try {
                channel.unregister(listener);

            } catch (IllegalArgumentException ignored) {
                // Don't need to do anything when a listener is not used.
            }
        }
    }

    @Override
    public void publish(Topic topic, Message message) {
        EventBus channel = channels.getOrDefault(topic, new EventBus()); //
        channel.post(message);
    }
}
