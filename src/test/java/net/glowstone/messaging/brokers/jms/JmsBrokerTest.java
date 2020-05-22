package net.glowstone.messaging.brokers.jms;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import net.glowstone.messaging.Subscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test the added functionality of the JmsBroker, this does not include the functionality of another library.
 */
public class JmsBrokerTest {

    private static final String TOPIC_ONE = "One";
    private static final String TOPIC_TWO = "two";

    private Connection connection;
    private JmsCodec jmsCodec;
    private MessageConsumer consumer;
    private MessageProducer producer;
    private Session session;

    private Subscriber alice, bob;
    private javax.jms.Topic topic1, topic2;

    private JmsBroker<String, Subscriber, String>  jmsBroker;


    /**
     * Create a simple JmsBroker with a mocked connection and codec. Other classes such as consumer, session and
     * topics are also mocked, because this is needed in order to test the code correctly.
     */
    @BeforeEach
    void beforeEach() throws JMSException {

        connection = mock(Connection.class);
        jmsCodec = mock(JmsCodec.class);
        consumer = mock(MessageConsumer.class);
        producer = mock(MessageProducer.class);
        session = mock(Session.class);

        alice = new Subscriber("Alice");
        bob = new Subscriber("Bob");
        topic1 = mock(javax.jms.Topic.class);
        topic2 = mock(javax.jms.Topic.class);

        when(connection.createSession(Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(session);
        when(session.createConsumer(any())).thenReturn(consumer);
        when(session.createProducer(any())).thenReturn(producer);
        when(session.createTopic(any())).thenReturn(topic1, topic2);

        jmsBroker = new JmsBroker(connection, jmsCodec);
    }

    /**
     * Verify that the connection is started and a session is created.
     */
    @Test
    void startupTest() throws JMSException {
        verify(connection).start();
        verify(connection).createSession(Mockito.anyBoolean(), Mockito.anyInt());
    }

    /**
     * Verify that closing the broker is correct.
     */
    @Test
    void closeTest() throws JMSException {
        jmsBroker.close();
        Mockito.verify(connection).close();
        Mockito.verify(session).close();
    }

    /**
     * Verify that a topic is created when someone tries to subscribe to a non-existing topic.
     */
    @Test
    void generateNewTopicTest() throws JMSException {
        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        verify(session, times(1)).createTopic(any());
    }

    /**
     * Verify that only a specific topic is not created multiple times when multiple people try to subscribe to a
     * non-existing topic.
     */
    @Test
    void generateTopicOnceTest() throws JMSException {

        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        jmsBroker.subscribe(TOPIC_ONE, bob, bob::onMessage);
        verify(session, times(1)).createTopic(any());

        jmsBroker.subscribe(TOPIC_TWO, alice, alice::onMessage);
        jmsBroker.subscribe(TOPIC_TWO, bob, bob::onMessage);
        verify(session, times(2)).createTopic(any());
    }

    /**
     * Verify that whenever a new topic is created, a new producer that handles the publishing of messages for that
     * topic is created (only once).
     */
    @Test
    void createProducer() throws JMSException {
        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        jmsBroker.subscribe(TOPIC_ONE, bob, bob::onMessage);
        verify(session, times(1)).createProducer(any());
    }

    /**
     * Verify that a consumer is only created once when subscribing multiple times.
     */
    @Test
    void createConsumerTest() throws JMSException {
        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        verify(session, times(1)).createConsumer(any());
    }

    /**
     * Verify that for each unique topic-subscriber pair a new consumer is created.
     */
    @Test
    void createNewConsumerPerTopic() throws JMSException {
        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        jmsBroker.subscribe(TOPIC_ONE, bob, bob::onMessage);

        jmsBroker.subscribe(TOPIC_TWO, alice, alice::onMessage);
        jmsBroker.subscribe(TOPIC_TWO, bob, bob::onMessage);
        jmsBroker.subscribe(TOPIC_TWO, bob, bob::onMessage);

        verify(session, times(4)).createConsumer(any());
    }

    /**
     * Verify that if all subscribes unsubscribed from a topic, the corresponding producer (or topic publisher) is
     * deleted. When a subscriber subscribes to the topic again, a new producer should be created again.
     */
    @Test
    void createNewProducerAfterEmptyTopic() throws JMSException {

        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        jmsBroker.subscribe(TOPIC_ONE, bob, bob::onMessage);

        jmsBroker.unsubscribe(TOPIC_ONE, alice);
        jmsBroker.unsubscribe(TOPIC_ONE, bob);

        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        verify(session, times(2)).createProducer(any());
    }

    /**
     * Verify that when a subscriber unsubscribes from a topic a new consumer is created correctly. This also
     * confirms deleting the old consumer is working correctly.
     */
    @Test
    void createNewConsumerAfterUnsubscribeTest() throws JMSException {

        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        verify(session, times(1)).createConsumer(any());

        jmsBroker.unsubscribe(TOPIC_ONE, alice);
        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        verify(session, times(2)).createConsumer(any());
    }

    /**
     * Verify that when a publisher tries to publish a message to a topic no one is subscribed to nothing happens.
     */
    @Test
    void noMessageSendTest() throws JMSException {
        jmsBroker.publish(TOPIC_ONE, "publish");
        verify(producer, never()).send(any());
    }

    /**
     * Verify that an encoded message is send when a publisher publishes a message to a topic that has a subscriber.
     */
    @Test
    void sendMessageTest() throws JMSException {

        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        jmsBroker.publish(TOPIC_ONE, "publish");

        verify(jmsCodec, times(1)).encode(any(), any());
        verify(producer, times(1)).send(any());
    }

    /**
     * Verify that if a jms exception is thrown when a topic is created, that an exception is also thrown when
     * trying to subscribe to such an topic.
     */
    @Test
    void exceptionOnTopicCreationTest() throws JMSException {
        when(session.createTopic(any())).thenThrow(new JMSException("Can't create topic"));
        assertThrows(RuntimeException.class, () -> jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage));
        assertThrows(RuntimeException.class, () -> jmsBroker.unsubscribe(TOPIC_ONE, alice));
    }

    /**
     * Verify that if a jms exception is thrown when a consumer is created, that an exception is also thrown when
     * trying to subscribe to such an topic.
     */
    @Test
    void exceptionOnConsumerCreationTest() throws JMSException {
        when(session.createConsumer(any())).thenThrow(new JMSException("Can't create consumer"));
        assertThrows(RuntimeException.class, () -> jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage));
        assertDoesNotThrow(() -> jmsBroker.unsubscribe(TOPIC_ONE, alice));
    }

    /**
     * Verify that when a jms exception is thrown when trying to encode a message, an exception is thrown when
     * publishing in the broker.
     */
    @Test
    void exceptionOnEncodingTest() throws JMSException {

        when(jmsCodec.encode(any(), any())).thenThrow(new JMSException("Failed to encode message"));

        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        assertThrows(RuntimeException.class, () -> jmsBroker.publish(TOPIC_ONE, "publish"));
    }
}
