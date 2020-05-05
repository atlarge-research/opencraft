package net.glowstone.messaging;

import java.util.function.Consumer;

public interface Broker<Topic, Subscriber, Message> {

    void subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback);

    void unsubscribe(Topic topic, Subscriber subscriber);

    void publish(Topic topic, Message message);
}
