package net.glowstone.messaging.brokers.jms;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import javax.jms.MessageConsumer;
import org.junit.jupiter.api.Test;

public class JmsSubscriberTest {

    /**
     * Verify that the equals method only checks the subscriber not the consumer when comparing.
     */
    @Test
    void equalsTest() {
        MessageConsumer consumer = mock(MessageConsumer.class);
        JmsSubscriber<String> sub1 = new JmsSubscriber<>(null, "alice");
        JmsSubscriber<String> sub2 = new JmsSubscriber<>(consumer, "alice");
        assertTrue(sub1.equals(sub2));
    }

    /**
     * Verify the equals method fails when consumers are the same, but the subscribers differ.
     */
    @Test
    void equalsFailTest() {
        MessageConsumer consumer = mock(MessageConsumer.class);
        JmsSubscriber<String> sub1 = new JmsSubscriber<>(consumer, "alice");
        JmsSubscriber<String> sub2 = new JmsSubscriber<>(consumer, "bob");
        assertFalse(sub1.equals(sub2));
    }


}
