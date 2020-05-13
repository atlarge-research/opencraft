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
     * Verify that a user can unsubscribe from a channel he/she was not subscribed to.
     */
    @Test
    void unsubscribeEmptyTest() {
        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");
        assertDoesNotThrow(() -> channel.unsubscribe(alice));
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
     * Verify that a subscribed user receives a published message.
     */
    @TimeBasedTest
    void subscribePublishTest() throws InterruptedException {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");
        String message = "Message";

        channel.subscribe(alice, alice::onMessage);
        channel.publish(message);

        alice.assertReceived(message);
    }

    /**
     * Verify that a subscribed user receives multiple published message.
     */
    @TimeBasedTest
    void subscribePublishMultipleTest() throws InterruptedException {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");
        String first = "First";
        String second = "Second";
        String third = "Third";

        channel.subscribe(alice, alice::onMessage);
        channel.publish(first);
        channel.publish(second);
        channel.publish(third);

        alice.assertReceivedAll(first, second, third);
    }

    /**
     * Verify that multiple subscribed users receive a published message.
     */
    @TimeBasedTest
    void subscribeMultiplePublishTest() throws InterruptedException {

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
     * Verify that an unsubscribed user does not receive a published message.
     */
    @TimeBasedTest
    void unsubscribedPublishTest() throws InterruptedException {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");
        String message = "Message";

        channel.publish(message);

        alice.assertNotReceived(message);
    }

    /**
     * Verify that a previously subscribed user does not receive a published message.
     */
    @TimeBasedTest
    void subscribeUnsubscribePublishTest() throws InterruptedException {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");
        String message = "Message";

        channel.subscribe(alice, alice::onMessage);
        channel.unsubscribe(alice);
        channel.publish(message);

        alice.assertNotReceived(message);
    }
}
