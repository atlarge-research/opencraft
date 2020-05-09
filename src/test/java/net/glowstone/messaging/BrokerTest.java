package net.glowstone.messaging;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public abstract class BrokerTest {

    protected abstract Broker<String, Subscriber, String> createBroker();

    /**
     * Verify whether a broker is initially created with no topics.
     */
    @Test
    void initiallyEmptyTest() {
        Broker<String, Subscriber, String> broker = createBroker();
        assertFalse(broker.exists("Topic"));
    }

    /**
     * Verify that subscribing to a non-existing topic creates the topic and correctly subscribes the user.
     */
    @Test
    void subscribeNonExistingTopicTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");

        assertDoesNotThrow(() -> broker.subscribe(topic, alice, alice::onMessage));
        assertTrue(broker.exists(topic));
        assertTrue(broker.isSubscribed(topic, alice));
    }

    /**
     * Verify that unsubscribing from a non-existing topic doesn't cause an exception.
     */
    @Test
    void unsubscribeNonExistingTopicTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        Subscriber alice = new Subscriber("Alice");

        assertDoesNotThrow(() -> broker.unsubscribe("Topic", alice));
    }

    /**
     * Verify that a single user can subscribe to and unsubscribe from a topic.
     */
    @Test
    void subscribeUnsubscribeTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");

        broker.subscribe(topic, alice, alice::onMessage);
        assertTrue(broker.isSubscribed(topic, alice));

        broker.unsubscribe(topic, alice);
        assertFalse(broker.isSubscribed(topic, alice));
    }

    /**
     * Verify that a multiple users can subscribe to and unsubscribe from a topic.
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
