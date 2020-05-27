package net.glowstone.messaging.brokers.channelbrokers.concurrent;

import net.glowstone.messaging.channels.ConcurrentChannel;
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
     * Verify that a channel's 'isEmpty()' method returns the correct value.
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
}
