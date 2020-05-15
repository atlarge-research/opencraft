package net.glowstone.messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The subscriber is a utility class to be used during the testing of Broker and Channel implementations.
 */
public final class Subscriber {

    /**
     * The maximum delay a subscriber should wait before receiving a message in milliseconds. The value 50 ms is chosen
     * since it is equal to the length of a single server tick.
     */
    private static final long MAX_DELAY = 50;

    private final String name;
    private final BlockingQueue<String> messages;

    /**
     * Create a subscriber that can receive multiple messages.
     */
    public Subscriber(String name) {
        this.name = name;
        messages = new LinkedBlockingQueue<>();
    }

    /**
     * The callback used to receive messages.
     *
     * @param message the message to be received.
     */
    public void onMessage(String message) {
        messages.offer(message);
    }

    /**
     * Verify that the expected message is received within the maximum delay.
     *
     * @param expected the expected message to be received.
     */
    public void assertReceived(String expected) throws InterruptedException {

        String actual = messages.poll(MAX_DELAY, TimeUnit.MILLISECONDS);
        if (actual == null) {
            fail(name + " expected to receive \"" + expected + "\", but did not receive any message.");
        }

        assertEquals(expected, actual, name + " expected to receive \"" + expected + "\", but received \"" + actual + "\".");
    }

    /**
     * Verify that the expected messages are received within the maximum delay.
     *
     * @param expected the expected messages to be received.
     */
    public void assertReceivedAll(String ... expected) throws InterruptedException {

        Set<String> send = new HashSet<>(Arrays.asList(expected));
        for (int index = 0; index < expected.length; index++) {

            String received = messages.poll(MAX_DELAY, TimeUnit.MILLISECONDS);
            if (received == null) {
                continue;
            }

            if (!send.remove(received)) {
                fail(name + " received unexpected message \"" + received + "\".");
            }
        }

        if (!send.isEmpty()) {
            StringJoiner joiner = new StringJoiner("\", \"");
            send.forEach(joiner::add);
            fail(name + " did not receive messages: \"" + joiner + "\".");
        }
    }

    /**
     * Verify that no message is received within the maximum delay, especially not the given message.
     *
     * @param unexpected the message that should not be received.
     */
    public void assertNotReceived(String unexpected) throws InterruptedException {

        String actual = messages.poll(MAX_DELAY, TimeUnit.MILLISECONDS);
        if (unexpected.equals(actual)) {
            fail(name + " expected not to receive the message \"" + unexpected + "\", but did.");
        }

        assertNull(actual, name + " expected not to receive a message, but received \"" + actual + "\".");
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
        return name;
    }
}
