package net.glowstone.messaging.channels;

import com.google.common.eventbus.Subscribe;
import java.util.function.Consumer;

/**
 * The guava listener is used for the callback function.
 *
 * @param <Message> the type of messages that can be listened for.
 */
@SuppressWarnings("UnstableApiUsage")
public final class GuavaListener<Message> {

    private final Consumer<Message> callback;

    /**
     * Construct a new guava listener.
     * @param callback the message callback.
     */
    public GuavaListener(Consumer<Message> callback) {
        this.callback = callback;
    }

    @Subscribe
    public void onMessage(Message message) {
        callback.accept(message);
    }
}
