package net.glowstone.messaging;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
     * Verify that subscribing to a non-existing topic does not cause an exception.
     */
    @Test
    void subscribeNonExistingTopicTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        Subscriber alice = new Subscriber("Alice");

        assertDoesNotThrow(() -> broker.subscribe("Topic", alice, alice::onMessage));
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
