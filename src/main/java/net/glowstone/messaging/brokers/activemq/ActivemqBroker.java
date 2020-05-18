package net.glowstone.messaging.brokers.activemq;

import javax.jms.JMSException;
import net.glowstone.messaging.brokers.jms.JmsBroker;
import net.glowstone.messaging.brokers.jms.JmsCodec;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * The ActiveMQ broker, this broker requires an ActiveMQ server to be running that wil handle the sending and
 * receiving of messages.
 *
 * @param <Topic> The type of topics that is allowed to identify jms topics.
 * @param <Subscriber> The type of subscribers that is allowed to subscribe to topics.
 * @param <Message> The type of messages that is allowed to be published to a jms topic.
 */
public class ActivemqBroker<Topic, Subscriber, Message> extends JmsBroker<Topic, Subscriber, Message> {

    /**
     * Create an ActiveMQ broker.
     *
     * @param uri The link used to connect to the ActiveMQ server.
     * @param codec the codec that has to be used for encoding and decoding messages.
     */
    public ActivemqBroker(String uri, JmsCodec<Message> codec) throws JMSException {
        super(new ActiveMQConnectionFactory(uri), codec);
    }

}
