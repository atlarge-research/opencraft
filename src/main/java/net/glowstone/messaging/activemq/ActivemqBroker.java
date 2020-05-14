package net.glowstone.messaging.activemq;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import javafx.util.Pair;
import javax.jms.*;
import net.glowstone.messaging.Broker;
import net.glowstone.util.config.ServerConfig;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActivemqBroker<Topic, Subscriber, Message> implements Broker<Topic, Subscriber, Message>, Closeable {

    private final String connectionURI = "tcp://localhost:" + ServerConfig.DEFAULT_PORT;
    private final Map<javax.jms.Topic, MessageProducer> topicMap;
    private final Map<Pair<javax.jms.Topic, Subscriber>, MessageConsumer> subscriberMap;
    private final Map<javax.jms.Topic, List<MessageConsumer>> consumerMap;
    private final ConnectionFactory connectionFactory;
    private int counter;

    private Connection connection;
    private Session session;

    private final Map<Topic, javax.jms.Topic> jmsTopics;

    public ActivemqBroker() throws JMSException {

        topicMap = new ConcurrentHashMap<>();
        subscriberMap = new ConcurrentHashMap<>();
        consumerMap = new ConcurrentHashMap<>();
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
        javax.jms.Topic topic = session.createTopic(name);
        MessageProducer publisher = session.createProducer(topic);
        topicMap.put(topic, publisher);
        consumerMap.put(topic, new ArrayList<>());
        return topic;
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
        javax.jms.Topic t = null;
        try {
            t = convert(topic);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        if (t == null) {
            return false;
        } else {
            Pair p = new Pair<>(t, subscriber);
            if(subscriberMap.containsKey(p)) {
                return false;
            }
            MessageConsumer sub = null;
            try {
                sub = session.createConsumer(t);
            } catch (JMSException e) {
                e.printStackTrace();
            }
            consumerMap.get(t).add(sub);
            subscriberMap.put(p, sub); // TODO: make subscriber class and add listener
        }
        return topicMap.get(t) != null;
    }

    @Override
    public boolean unsubscribe(Topic topic, Subscriber subscriber) {
        javax.jms.Topic t = null;
        try {
            t = convert(topic);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        Pair p = new Pair(t, subscriber);
        if (subscriberMap.containsKey(p)) {
            MessageConsumer messageConsumer = subscriberMap.remove(p);
            consumerMap.get(t).remove(messageConsumer);
            if (consumerMap.get(t).isEmpty()) {
                consumerMap.remove(t);
                MessageProducer producer = topicMap.remove(t);
                try {
                    producer.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void publish(Topic topic, Message message) {
        javax.jms.Topic t = null;
        javax.jms.ObjectMessage mess = null;
        try {
            t = convert(topic);
            mess = session.createObjectMessage(); // TODO: not an actual message
            topicMap.get(t).send(mess);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
