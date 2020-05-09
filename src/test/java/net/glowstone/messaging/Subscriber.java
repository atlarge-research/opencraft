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
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Subscriber(name=\"" + name + "\")";
    }
}
