package net.glowstone.messaging.brokers.jms;

import java.io.Closeable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import net.glowstone.messaging.Broker;

public class JmsBroker<Topic, Subscriber, Message> implements Broker<Topic, Subscriber, Message>, Closeable {

    private final Connection connection;
    private final Session session;
    private final JmsSerializer<Message> serializer;

    private final Map<Topic, javax.jms.Topic> jmsTopics;
    private final AtomicInteger counter;

    private final Map<javax.jms.Topic, MessageProducer> publishers;
    private final Map<javax.jms.Topic, Set<JmsSubscriber<Subscriber>>> subscribers;
    private final Lock lock;

    public JmsBroker(ConnectionFactory connectionFactory, JmsSerializer<Message> serializer) throws JMSException {

        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.serializer = serializer;

        jmsTopics = new HashMap<>();
        counter = new AtomicInteger();

        publishers = new HashMap<>();
        subscribers = new HashMap<>();
        lock = new ReentrantLock();
    }

    private javax.jms.Topic convert(Topic topic) throws JMSException {
        javax.jms.Topic jmsTopic = jmsTopics.get(topic);
        if(jmsTopic == null) {
            jmsTopic = generate();
            jmsTopics.put(topic, jmsTopic);
        }
        return jmsTopic;
    }

    private javax.jms.Topic generate() throws JMSException {
        int id = counter.getAndIncrement();
        String name = String.valueOf(id);
        return session.createTopic(name);
    }

    @Override
    public void subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback) {

        lock.lock();
        try {
            javax.jms.Topic jmsTopic = convert(topic);
            MessageConsumer consumer = session.createConsumer(jmsTopic);
            consumer.setMessageListener(jmsMessage -> {
                Message message = serializer.deserialize(session, jmsMessage);
                callback.accept(message);
            });
            JmsSubscriber<Subscriber> sub = new JmsSubscriber<>(consumer, subscriber);

            Set<JmsSubscriber<Subscriber>> topicSubscribers = subscribers.get(jmsTopic);
            if (topicSubscribers == null) {

                MessageProducer publisher = session.createProducer(jmsTopic);
                publishers.put(jmsTopic, publisher);

                topicSubscribers = new HashSet<>();
                subscribers.put(jmsTopic, topicSubscribers);
            }
            topicSubscribers.add(sub);

        } catch (JMSException e) {
            throw new RuntimeException("Failed to subscribe to JMS broker", e);

        } finally {
            lock.unlock();
        }
    }

    @Override
    public void unsubscribe(Topic topic, Subscriber subscriber) {
        lock.lock();
        try {
            javax.jms.Topic jmsTopic = convert(topic);
            Set<JmsSubscriber<Subscriber>> topicSubscribers = subscribers.get(jmsTopic);
            if (topicSubscribers != null) {
                JmsSubscriber<Subscriber> sub = new JmsSubscriber<>(null, subscriber);
                topicSubscribers.remove(sub);
                if (topicSubscribers.isEmpty()) {
                    publishers.remove(jmsTopic);
                }
            }

        } catch (JMSException e) {
            throw new RuntimeException("Failed to unsubscribe from JMS broker", e);

        } finally {
            lock.unlock();
        }
    }

    @Override
    public void publish(Topic topic, Message message) {
        lock.lock();
        try {
            javax.jms.Topic jmsTopic = convert(topic);
            javax.jms.Message jmsMessage = serializer.serialize(session, message);
            MessageProducer producer = publishers.get(jmsTopic);
            producer.send(jmsMessage);

        } catch (JMSException e) {
            throw new RuntimeException("Failed to publish to JMS broker", e);

        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() {
        try {
            session.close();
            connection.stop();
            connection.close();
        } catch (JMSException e) {
            throw new RuntimeException("Failed to close JMS broker", e);
        }
    }
}
