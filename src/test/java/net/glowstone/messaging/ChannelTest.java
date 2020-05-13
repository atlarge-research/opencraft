package net.glowstone.messaging;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

/**
 * The ChannelTest class provides tests for the general use cases of all channels. Preventing a code-duplication and
 * thereby programming mistakes.
 */
public abstract class ChannelTest {

    /**
     * Create a channel to be used in the general use case tests.
     *
     * @return a channel instance of the implementing class.
     */
    protected abstract Channel<Subscriber, String> createChannel();

    /**
     * Verify that a message can be published to a channel with no subscribers.
     */
    @Test
    void publishEmptyTest() {
        Channel<Subscriber, String> channel = createChannel();
        assertDoesNotThrow(() -> channel.publish("Message"));
    }

    /**
     * Verify that a subscriber receives a publish message.
     */
    @Test
    void subscribePublishTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");
        String message = "Message";

        channel.subscribe(alice, alice::onMessage);
        channel.publish(message);

        alice.assertReceived(message);
    }

    /**
     * Verify that multiple subscribers receive a published message.
     */
    @Test
    void broadcastTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");
        Subscriber bob = new Subscriber("Bob");
        String message = "Message";

        channel.subscribe(alice, alice::onMessage);
        channel.subscribe(bob, bob::onMessage);
        channel.publish(message);

        alice.assertReceived(message);
        bob.assertReceived(message);
    }
}
