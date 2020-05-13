package net.glowstone.messaging;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     * Verify that a created channel is initially empty.
     */
    @Test
    void initiallyEmptyTest() {
        Channel<Subscriber, String> channel = createChannel();
        assertTrue(channel.isEmpty());
    }

    /**
     * Verify that the state of a channel is correctly presented by its 'isEmpty()' method.
     */
    @Test
    void emptyTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");

        channel.subscribe(alice, alice::onMessage);
        assertFalse(channel.isEmpty());

        channel.unsubscribe(alice);
        assertTrue(channel.isEmpty());
    }

    /**
     * Verify that a user can subscribe to a channel.
     */
    @Test
    void subscribeTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");

        channel.subscribe(alice, alice::onMessage);
        assertTrue(channel.isSubscribed(alice));
    }

    /**
     * Verify that a correct value is returned on subscription.
     */
    @Test
    void subscribeTwiceTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");

        assertTrue(channel.subscribe(alice, alice::onMessage));
        assertFalse(channel.subscribe(alice, alice::onMessage));
    }

    /**
     * Verify that a user can unsubscribe from a channel.
     */
    @Test
    void unsubscribeTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");

        channel.subscribe(alice, alice::onMessage);

        channel.unsubscribe(alice);
        assertFalse(channel.isSubscribed(alice));
    }

    /**
     * Verify that a correct value is returned on unsubscription.
     */
    @Test
    void unsubscribeTwiceTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");

        channel.subscribe(alice, alice::onMessage);

        assertTrue(channel.unsubscribe(alice));
        assertFalse(channel.unsubscribe(alice));
    }

    /**
     * Verify that multiple users can subscribe and unsubscribe.
     */
    @Test
    void subscribeManyUnsubscribeOneTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");
        Subscriber bob = new Subscriber("Bob");

        channel.subscribe(alice, alice::onMessage);
        channel.subscribe(bob, bob::onMessage);
        assertTrue(channel.isSubscribed(alice));
        assertTrue(channel.isSubscribed(bob));

        channel.unsubscribe(alice);
        assertFalse(channel.isSubscribed(alice));
        assertTrue(channel.isSubscribed(bob));
    }

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
