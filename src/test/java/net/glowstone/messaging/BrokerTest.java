package net.glowstone.messaging;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The BrokerTest class provides tests for the general use cases of all brokers. Preventing a code-duplication and
 * thereby programming mistakes.
 */
public abstract class BrokerTest {

    /**
     * Create a broker to be used in the general use case tests.
     *
     * @return a broker instance of the implementing class.
     */
    protected abstract Broker<String, Subscriber, String> createBroker();

    /**
     * Verify that a created broker is initially empty.
     */
    @Test
    void initiallyEmptyTest() {
        Broker<String, Subscriber, String> broker = createBroker();
        Assertions.assertTrue(broker.isEmpty());
    }

    /**
     * Verify that the state of a broker is correctly reflected by its 'isEmpty()' method.
     */
    @Test
    void emptyTest() {

        Broker<String, Subscriber, String> broker = createBroker();
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

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");

        broker.subscribe(topic, alice, alice::onMessage);
        assertTrue(broker.isSubscribed(topic, alice));
    }

    /**
     * Verify that a correct value is returned on subscription.
     */
    @Test
    void subscribeTwiceTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");

        assertTrue(broker.subscribe(topic, alice, alice::onMessage));
        assertFalse(broker.subscribe(topic, alice, alice::onMessage));
    }

    /**
     * Verify that subscribing to a non-existing topic does not cause an exception.
     */
    @Test
    void subscribeNonExistingTopicTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        Subscriber alice = new Subscriber("Alice");

        assertDoesNotThrow(() -> broker.subscribe("Topic", alice, alice::onMessage));
    }

    /**
     * Verify that a user can unsubscribe from a topic.
     */
    @Test
    void unsubscribeTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");

        broker.subscribe(topic, alice, alice::onMessage);

        broker.unsubscribe(topic, alice);
        assertFalse(broker.isSubscribed(topic, alice));
    }

    /**
     * Verify that a correct value is returned on unsubscription.
     */
    @Test
    void unsubscribeTwiceTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");

        broker.subscribe(topic, alice, alice::onMessage);

        assertTrue(broker.unsubscribe(topic, alice));
        assertFalse(broker.unsubscribe(topic, alice));
    }

    /**
     * Verify that unsubscribing from a non-existing topic does not cause an exception.
     */
    @Test
    void unsubscribeNonExistingTopicTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        Subscriber alice = new Subscriber("Alice");

        assertDoesNotThrow(() -> broker.unsubscribe("Topic", alice));
    }

    /**
     * Verify that multiple users can subscribe to and unsubscribe from a topic.
     */
    @Test
    void subscribeManyUnsubscribeOneTest() {

        Broker<String, Subscriber, String> broker = createBroker();
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

    /**
     * Verify that publishing to a non-existing topic doesn't cause an exception.
     */
    @Test
    void publishNonExistingChannelTest() {
        Broker<String, Subscriber, String> broker = createBroker();
        assertDoesNotThrow(() -> broker.publish("Topic", "Message"));
    }

    /**
     * Verify that a single subscriber receives a published message.
     */
    @Test
    void subscribePublishTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");
        String message = "Message";

        broker.subscribe(topic, alice, alice::onMessage);
        broker.publish(topic, message);

        alice.assertReceived(message);
    }

    /**
     * Verify that multiple subscribers receive a published message.
     */
    @Test
    void broadcastTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");
        Subscriber bob = new Subscriber("Bob");
        String message = "Message";

        broker.subscribe(topic, alice, alice::onMessage);
        broker.subscribe(topic, bob, bob::onMessage);
        broker.publish(topic, message);

        alice.assertReceived(message);
        bob.assertReceived(message);
    }
}
