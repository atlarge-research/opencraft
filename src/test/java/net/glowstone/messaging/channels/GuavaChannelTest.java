package net.glowstone.messaging.channels;

import net.glowstone.messaging.channels.GuavaChannel;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.glowstone.messaging.ChannelTest;
import net.glowstone.messaging.Subscriber;
import org.junit.jupiter.api.Test;

public class GuavaChannelTest extends ChannelTest {

    @Override
    protected GuavaChannel<Subscriber, String> createChannel() {
        return new GuavaChannel<>();
    }

    /**
     * Verify that a created channel is initially empty.
     */
    @Test
    void initiallyEmptyTest() {
        GuavaChannel<Subscriber, String> channel = createChannel();
        assertTrue(channel.isEmpty());
    }

    /**
     * Verify that a channel's 'isEmpty()' method returns the correct value.
     */
    @Test
    void emptyTest() {

        GuavaChannel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");

        channel.subscribe(alice, alice::onMessage);
        assertFalse(channel.isEmpty());

        channel.unsubscribe(alice);
        assertTrue(channel.isEmpty());
    }
}
