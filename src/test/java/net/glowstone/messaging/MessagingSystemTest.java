package net.glowstone.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The messaging system is a simple class with less simple dependencies. Therefore, mocking is used to determine whether
 * the interaction between publishers and subscribers with the messaging system is handled correctly.
 */
final class MessagingSystemTest {

    private static final String OLD_TOPIC = "Old";
    private static final String NEW_TOPIC = "New";

    private static final Set<String> EMPTY = new HashSet<>();
    private static final Set<String> OLD_TOPICS = Collections.singleton(OLD_TOPIC);
    private static final Set<String> NEW_TOPICS = Collections.singleton(NEW_TOPIC);

    private Policy<String, String, Subscriber> policy;
    private Broker<String, Subscriber, String> broker;
    private MessagingSystem<String, String, Subscriber, String> system;
    private Subscriber alice;

    /**
     * Setup the policy, broker, messaging system, and subscriber used during testing.
     */
    @BeforeEach
    @SuppressWarnings("unchecked")
    void beforeEach() {
        policy = (Policy<String, String, Subscriber>) mock(Policy.class);
        broker = (Broker<String, Subscriber, String>) mock(Broker.class);
        system = new MessagingSystem<>(policy, broker);
        alice = new Subscriber("Alice");
    }

    /**
     * Verify that a user with no interests does not interact with the broker.
     */
    @Test
    void noInterests() {

        when(policy.computeInterestSet(alice)).thenReturn(EMPTY);
        system.update(alice, alice::onMessage);

        verify(broker, never()).unsubscribe(any(), any());
        verify(broker, never()).subscribe(any(), any(), any());
    }

    /**
     * Verify that a user with new interests is correctly subscribed.
     */
    @Test
    void AddNewInterests() {

        when(policy.computeInterestSet(alice)).thenReturn(NEW_TOPICS);
        system.update(alice, alice::onMessage);

        verify(broker, never()).unsubscribe(any(), any());
        verify(broker).subscribe(eq(NEW_TOPIC), eq(alice), any());
    }

    /**
     * Verify that a user with old interests is correctly unsubscribed.
     */
    @Test
    @SuppressWarnings("unchecked")
    void clearOldInterests() {

        when(policy.computeInterestSet(alice)).thenReturn(OLD_TOPICS);
        system.update(alice, alice::onMessage);

        reset(broker);

        when(policy.computeInterestSet(alice)).thenReturn(EMPTY);
        system.update(alice, alice::onMessage);

        verify(broker).unsubscribe(eq(OLD_TOPIC), eq(alice));
        verify(broker, never()).subscribe(any(), any(), any());
    }

    /**
     * Verify that a user with both old and new interests is correctly subscribed and unsubscribed.
     */
    @Test
    @SuppressWarnings("unchecked")
    void updateInterests() {

        when(policy.computeInterestSet(alice)).thenReturn(OLD_TOPICS);
        system.update(alice, alice::onMessage);

        reset(broker);

        when(policy.computeInterestSet(alice)).thenReturn(NEW_TOPICS);
        system.update(alice, alice::onMessage);

        verify(broker).unsubscribe(eq(OLD_TOPIC), eq(alice));
        verify(broker).subscribe(eq(NEW_TOPIC), eq(alice), any());
    }

    /**
     * Verify that a publisher broadcasting a message publishes it to the correct topic.
     */
    @Test
    void broadcastTest() {

        String publisher = "Publisher";
        Iterable<String> topics = Collections.singleton("Topic");
        String message = "Message";

        when(policy.selectTargets(publisher)).thenReturn(topics);
        system.broadcast(publisher, message);

        verify(broker).publish("Topic", message);
    }
}
