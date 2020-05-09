package net.glowstone.messaging.concurrent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.glowstone.messaging.ChannelTest;
import net.glowstone.messaging.Subscriber;
import org.junit.jupiter.api.Test;

public class ConcurrentChannelTest extends ChannelTest {

    @Override
    protected ConcurrentChannel<Subscriber, String> createChannel() {
        return new ConcurrentChannel<>();
    }

    /**
     * Verify that subscribing and unsubscribing alters the number of subscriptions.
     */
    @Test
    void subscribeAndUnsubscribeTest() {

        ConcurrentChannel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");

        assertTrue(channel.isEmpty());
        channel.subscribe(alice, alice::onMessage);
        assertFalse(channel.isEmpty());
        channel.unsubscribe(alice);
        assertTrue(channel.isEmpty());
    }
}
