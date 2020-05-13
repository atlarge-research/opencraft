package net.glowstone.messaging.brokers.concurrent;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import net.glowstone.messaging.BrokerTest;
import net.glowstone.messaging.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class ConcurrentBrokerTest extends BrokerTest {

    @Override
    protected ConcurrentBroker<String, Subscriber, String> createBroker() {
        return new ConcurrentBroker<>();
    }

    /**
     * Verify that a created broker is initially empty.
     */
    @Test
    void initiallyEmptyTest() {
        ConcurrentBroker<String, Subscriber, String> broker = createBroker();
        Assertions.assertTrue(broker.isEmpty());
    }

    /**
     * Verify that the state of a broker is correctly reflected by its 'isEmpty()' method.
     */
    @Test
    void emptyTest() {

        ConcurrentBroker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");

        broker.subscribe(topic, alice, alice::onMessage);
        assertFalse(broker.isEmpty());

        broker.unsubscribe(topic, alice);
        Assertions.assertTrue(broker.isEmpty());
    }

    /**
     * Verify that a user can subscribe to a topic.
     */
    @Test
    void subscribeTest() {

        ConcurrentBroker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");

        broker.subscribe(topic, alice, alice::onMessage);
        assertTrue(broker.isSubscribed(topic, alice));
    }

    /**
     * Verify that a user can unsubscribe from a topic.
     */
    @Test
    void unsubscribeTest() {

        ConcurrentBroker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");

        broker.subscribe(topic, alice, alice::onMessage);

        broker.unsubscribe(topic, alice);
        assertFalse(broker.isSubscribed(topic, alice));
    }

    /**
     * Verify that multiple users can subscribe to and unsubscribe from a topic.
     */
    @Test
    void subscribeManyUnsubscribeOneTest() {

        ConcurrentBroker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");
        Subscriber bob = new Subscriber("Bob");

        broker.subscribe(topic, alice, alice::onMessage);
        broker.subscribe(topic, bob, bob::onMessage);

        assertTrue(broker.isSubscribed(topic, alice));
        assertTrue(broker.isSubscribed(topic, bob));

        broker.unsubscribe(topic, alice);
        assertFalse(broker.isSubscribed(topic, alice));
        assertTrue(broker.isSubscribed(topic, bob));
    }
}
