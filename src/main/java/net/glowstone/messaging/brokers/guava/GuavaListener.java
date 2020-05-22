package net.glowstone.messaging.brokers.guava;

import com.google.common.eventbus.Subscribe;
import java.util.function.Consumer;

/**
 *The guava listener is used for the callback function.
 *
 * @param <Message> the type of messages that is allowed to be published to a channel.
 */
@SuppressWarnings("UnstableApiUsage")
public class GuavaListener<Message> {

    private final Consumer<Message> callback;

    /**
     * Construct a new guava listener.
     * @param callback the message callback.
     */
    public GuavaListener(Consumer<Message> callback) {
        this.callback = callback;
    }

    @Subscribe
    public void handler(Message message) {
        callback.accept(message);
    }
}
