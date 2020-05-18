package net.glowstone.messaging.brokers.jms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import net.glowstone.command.minecraft.ToggleDownfallCommand;
import net.glowstone.messaging.Subscriber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

/**
 * Test the added functionality of the JmsBroker, this does not include the functionality of another library.
 */
public class JmsBrokerTest {

    private static final String TOPIC_ONE = "One";
    private static final String TOPIC_TWO = "two";

    private Connection connection;
    private JmsCodec jmsCodec;
    private JmsBroker<String, Subscriber, String>  jmsBroker;
    private Session session;
    private Subscriber alice, bob;
    private javax.jms.Topic topic;
    MessageConsumer consumer;


    /**
     * Create a simple JmsBroker with a mocked connection and codec.
     * Other classes such as consumer,session and topics are also mocked, because this is needed in order to test the
     * code correctly.
     */
    @BeforeEach
    @SuppressWarnings("unchecked")
    void beforeEach() throws JMSException {
        connection = mock(Connection.class);
        jmsCodec = mock(JmsCodec.class);
        consumer = mock(MessageConsumer.class);
        session = mock(Session.class);

        when(connection.createSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
        when(session.createConsumer(any())).thenReturn(consumer);

        jmsBroker = new JmsBroker(connection, jmsCodec);

        alice = new Subscriber("Alice");
        bob = new Subscriber("Bob");
        topic = mock(javax.jms.Topic.class);

        when(session.createTopic(any())).thenReturn(topic);

    }

    /**
     * Verify that the connection is started and a session is created.
     */
    @Test
    void startupTest() throws JMSException {
        Mockito.verify(connection).start();
        Mockito.verify(connection).createSession(false, Session.AUTO_ACKNOWLEDGE);
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

    @Test
    void createConsumerTest() throws JMSException {
        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        jmsBroker.subscribe(TOPIC_ONE, alice, alice::onMessage);
        verify(session, times(1)).createConsumer(any());
    }




}
