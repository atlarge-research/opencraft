package net.glowstone.messaging;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * The subscriber is a utility class to be used during the testing of Broker and Channel implementations.
 */
public final class Subscriber {

    private final String name;
    private final CompletableFuture<String> future;

    /**
     * Create a subscriber that can receive a single message.
     */
    public Subscriber(String name) {
        this.name = name;
        future = new CompletableFuture<>();
    }

    /**
     * Get the subscriber's name.
     *
     * @return the subscriber's name.
     */
    public String getName() {
        return name;
    }

    /**
     * The callback used to receive messages.
     *
     * @param message the message to be received.
     */
    public void onMessage(String message) {
        future.complete(message);
    }

    /**
     * Verify that the expected message is received before or within 1 second from now.
     *
     * @param expected the expected message to be received.
     */
    public void assertReceived(String expected) {
        assertDoesNotThrow(() -> {
            String actual = future.get(1, TimeUnit.SECONDS);
            assertEquals(expected, actual);
        });
    }

    @Override
    public boolean equals(Object other) {
        return other == this;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Subscriber(name=\"" + name + "\")";
    }
}
