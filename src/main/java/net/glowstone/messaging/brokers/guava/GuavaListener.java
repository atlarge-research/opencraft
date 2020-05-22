package net.glowstone.messaging.brokers.guava;

import com.google.common.eventbus.Subscribe;
import java.util.function.Consumer;

public class GuavaListener<Message> {

    private final Consumer<Message> callback;

    public GuavaListener(Consumer<Message> callback) {
        this.callback = callback;
    }

    @Subscribe
    public void handler(Message message) {
        callback.accept(message);
    }
}
