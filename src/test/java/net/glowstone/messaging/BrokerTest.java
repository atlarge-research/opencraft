package net.glowstone.messaging;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public abstract class BrokerTest {

    protected abstract Broker<String, Subscriber, String> createBroker();

    @Test
    void initiallyEmptyTest() {
        Broker<String, Subscriber, String> broker = createBroker();
        assertFalse(broker.exists("Topic"));
    }

    @Test
    void subscribeNonExistingTopicTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");

        assertDoesNotThrow(() -> broker.subscribe(topic, alice, alice::onMessage));
        assertTrue(broker.exists(topic));
        assertTrue(broker.isSubscribed(topic, alice));
    }

    @Test
    void unsubscribeNonExistingTopicTest() {

        Broker<String, Subscriber, String> broker = createBroker();
        Subscriber alice = new Subscriber("Alice");

        assertDoesNotThrow(() -> broker.unsubscribe("Topic", alice));
    }

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

    @Test
    void publishNonExistingChannelTest() {
        Broker<String, Subscriber, String> broker = createBroker();
        assertDoesNotThrow(() -> broker.publish("Topic", "Message"));
    }

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
