package net.glowstone.messaging;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

public abstract class ChannelTest {

    protected abstract Channel<Subscriber, String> createChannel();

    /**
     * Verify that a subscriber receives a publish message.
     */
    @Test
    void subscribeAndPublishTest() {

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

    /**
     * Verify that a message can be published to a channel with no subscribers.
     */
    @Test
    void publishToEmptyTest() {
        Channel<Subscriber, String> channel = createChannel();
        assertDoesNotThrow(() -> channel.publish("Message"));
    }
}
