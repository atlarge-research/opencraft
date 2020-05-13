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
     * Verify that publishing to a non-existing topic does not cause an exception.
     */
    @Test
    void publishNonExistingChannelTest() {
        Broker<String, Subscriber, String> broker = createBroker();
        assertDoesNotThrow(() -> broker.publish("Topic", "Message"));
    }

    /**
     * Verify that a subscribed user receives a published message.
     */
    @TimeBasedTest
    void subscribePublishTest() throws InterruptedException{

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");
        String message = "Message";

        broker.subscribe(topic, alice, alice::onMessage);
        broker.publish(topic, message);

        alice.assertReceived(message);
    }

    /**
     * Verify that a subscribed user receives a multiple published message.
     */
    @TimeBasedTest
    void subscribePublishMultipleTest() throws InterruptedException{

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");
        String first = "First";
        String second = "Second";

        broker.subscribe(topic, alice, alice::onMessage);
        broker.publish(topic, first);
        broker.publish(topic, second);

        alice.assertReceivedAll(first, second);
    }

    /**
     * Verify that multiple subscribed users receive a published message.
     */
    @TimeBasedTest
    void subscribeMultiplePublishTest() throws InterruptedException{

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");
        Subscriber bob = new Subscriber("Bob");
        String message = "Message";

        broker.subscribe(topic, alice, alice::onMessage);
        broker.subscribe(topic, bob, bob::onMessage);
        broker.publish(topic, message);

        alice.assertReceived(message);
    }

    /**
     * Verify that an unsubscribed user does not receive a published message.
     */
    @TimeBasedTest
    void unsubscribedPublishTest() throws InterruptedException{

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");
        String message = "Message";

        broker.publish(topic, message);

        alice.assertNotReceived(message);
    }

    /**
     * Verify that a previously subscribed user does not receive a published message.
     */
    @TimeBasedTest
    void subscribeUnsubscribePublishTest() throws InterruptedException{

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");
        String message = "Message";

        broker.subscribe(topic, alice, alice::onMessage);
        broker.unsubscribe(topic, alice);
        broker.publish(topic, message);

        alice.assertNotReceived(message);
    }

    /**
     * Verify that a previously subscribed user does not receive a published message.
     */
    @TimeBasedTest
    void subscribeMultipleUnsubscribePublishTest() throws InterruptedException{

        Broker<String, Subscriber, String> broker = createBroker();
        String topic = "Topic";
        Subscriber alice = new Subscriber("Alice");
        Subscriber bob = new Subscriber("Bob");
        String message = "Message";

        broker.subscribe(topic, alice, alice::onMessage);
        broker.subscribe(topic, bob, bob::onMessage);
        broker.unsubscribe(topic, alice);
        broker.publish(topic, message);

        alice.assertNotReceived(message);
        bob.assertReceived(message);
    }

    /**
     * Verify that a subscribed user receives a published message from multiple topics.
     */
    @TimeBasedTest
    void subscribeMultipleTopicsPublishTest() throws InterruptedException{

        Broker<String, Subscriber, String> broker = createBroker();
        String firstTopic = "First Topic";
        String secondTopic = "Second Topic";
        Subscriber alice = new Subscriber("Alice");
        String message = "Message";

        broker.subscribe(firstTopic, alice, alice::onMessage);
        broker.subscribe(secondTopic, alice, alice::onMessage);

        broker.publish(firstTopic, message);
        alice.assertReceived(message);

        broker.publish(secondTopic, message);
        alice.assertReceived(message);
    }

    /**
     * Verify that a subscribed user only receives a published message from the correct topic.
     */
    @TimeBasedTest
    void subscribeSpecificTopicPublishTest() throws InterruptedException{

        Broker<String, Subscriber, String> broker = createBroker();
        String first = "First";
        String second = "Second";
        Subscriber alice = new Subscriber("Alice");
        String message = "Message";

        broker.subscribe(first, alice, alice::onMessage);

        broker.publish(first, message);
        alice.assertReceived(message);

        broker.publish(second, message);
        alice.assertNotReceived(message);
    }
}
