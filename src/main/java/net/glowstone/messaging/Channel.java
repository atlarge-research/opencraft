package net.glowstone.messaging;

import java.util.function.Consumer;

public interface Channel<Subscriber, Message> {

    void subscribe(Subscriber subscriber, Consumer<Message> callback);

    void unsubscribe(Subscriber subscriber);

    void publish(Message message);
}
