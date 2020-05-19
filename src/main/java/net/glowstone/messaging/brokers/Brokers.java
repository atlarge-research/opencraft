package net.glowstone.messaging.brokers;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import net.glowstone.messaging.Broker;
import net.glowstone.messaging.brokers.concurrent.ConcurrentBroker;
import net.glowstone.messaging.brokers.jms.JmsBroker;
import net.glowstone.messaging.brokers.jms.JmsCodec;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * A factory class to for creating multiple types of brokers.
 */
public class Brokers {

    /**
     * Create a ConcurrentBroker.
     *
     * @param <Topic> The type of topics that is allowed to identify channels.
     * @param <Subscriber> The type of subscribers that is allowed to subscribe to a channel.
     * @param <Message> The type of messages that is allowed to be published to a channel.
     * @return
     */
    public static <Topic, Subscriber, Message> Broker<Topic, Subscriber, Message> newConcurrentBroker() {
        return new ConcurrentBroker<>();
    }

    /**
     * The ActiveMQ broker, this broker requires an ActiveMQ server to be running that wil handle the sending and
     * receiving of messages.
     *
     * @param uri The link used to connect to the ActiveMQ server.
     * @param codec The codec that has to be used for encoding and decoding messages.
     * @param <Message> The type of messages that is allowed to be published to a jms topic.
     * @return The ActiveMQ broker.
     */
    public static <Message> JmsBroker newActivemqBroker(String uri, JmsCodec<Message> codec) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(uri);
        Connection connection = factory.createConnection();
        return new JmsBroker(connection, codec);
    }

    /**
     * The constructor is private to prevent the initialization of the factory class.
     */
    private Brokers() {}
}
