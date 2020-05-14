package net.glowstone.messaging.activemq;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import net.glowstone.messaging.Broker;
import net.glowstone.util.config.ServerConfig;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActivemqBroker<Topic, Subscriber, Message> implements Broker<Topic, Subscriber, Message> {

    private final String connectionURI = "tcp://localhost:" + ServerConfig.DEFAULT_PORT;
    private final ConnectionFactory connectionFactory;
    private int counter;

    private Connection connection;
    private Session session;

    private final Map<Topic, javax.jms.Topic> jmsTopics;

    public ActivemqBroker() throws JMSException {

        connectionFactory = new ActiveMQConnectionFactory(connectionURI);
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        jmsTopics = new ConcurrentHashMap<>();
        counter = 0;
    }

    protected javax.jms.Topic convert(Topic topic) throws JMSException {
        AtomicReference<JMSException> exception = new AtomicReference<>(null);
        javax.jms.Topic d = jmsTopics.computeIfAbsent(topic, t -> {
            try {
                return generate();
            } catch (JMSException e) {
                exception.set(e);
                return null;
            }
        });
        JMSException e = exception.get();
        if (e != null) {
            throw e;
        }
        return d;
    }

    private javax.jms.Topic generate() throws JMSException {
        counter++;
        String name = String.valueOf(counter);
        return session.createTopic(name);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isSubscribed(Topic topic, Subscriber subscriber) {
        return false;
    }

    @Override
    public boolean subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback) {


        return false;
    }

    @Override
    public boolean unsubscribe(Topic topic, Subscriber subscriber) {
        return false;
    }

    @Override
    public void publish(Topic topic, Message message) {

    }

    @Override
    public void close() throws IOException {

    }
}
