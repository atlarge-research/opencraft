package net.glowstone.messaging.brokers.guava;

import com.google.common.eventbus.Subscribe;
import java.util.Objects;
import java.util.function.Consumer;

public class GuavaListener<Subscriber, Message> {

    private final Subscriber subscriber;
    private final Consumer<Message> callback;

    public GuavaListener(Subscriber subscriber, Consumer<Message> callback) {
        this.subscriber = subscriber;
        this.callback = callback;
    }

    @Subscribe
    public void handler(Message message) {
        callback.accept(message);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GuavaListener<?, ?> that = (GuavaListener<?, ?>) object;
        return Objects.equals(this.subscriber, that.subscriber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callback, subscriber);
    }
}
