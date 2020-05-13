package net.glowstone.messaging.brokers.concurrent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.glowstone.messaging.ChannelTest;
import net.glowstone.messaging.Subscriber;
import org.junit.jupiter.api.Test;

final class ConcurrentChannelTest extends ChannelTest {

    @Override
    protected ConcurrentChannel<Subscriber, String> createChannel() {
        return new ConcurrentChannel<>();
    }

    /**
     * Verify that a created channel is initially empty.
     */
    @Test
    void initiallyEmptyTest() {
        ConcurrentChannel<Subscriber, String> channel = createChannel();
        assertTrue(channel.isEmpty());
    }

    /**
     * Verify that the state of a channel is correctly presented by its 'isEmpty()' method.
     */
    @Test
    void emptyTest() {

        ConcurrentChannel<Subscriber, String> channel = createChannel();
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

        ConcurrentChannel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");

        channel.subscribe(alice, alice::onMessage);
        assertTrue(channel.isSubscribed(alice));
    }

    /**
     * Verify that a user can unsubscribe from a channel.
     */
    @Test
    void unsubscribeTest() {

        ConcurrentChannel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");

        channel.subscribe(alice, alice::onMessage);

        channel.unsubscribe(alice);
        assertFalse(channel.isSubscribed(alice));
    }

    /**
     * Verify that multiple users can subscribe and unsubscribe.
     */
    @Test
    void subscribeManyUnsubscribeOneTest() {

        ConcurrentChannel<Subscriber, String> channel = createChannel();
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
}
