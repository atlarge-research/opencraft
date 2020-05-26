package net.glowstone.messaging.brokers.jms;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import net.glowstone.messaging.Broker;

/**
 * The jms broker uses a connection to communicate between publishers and subscribers. This connection is the clients
 * link to a JMS provider which often allocates resources outside the JVM. The connection object often serves as a
 * TCP/IP socket. The session is used to create publishers, subscribers and jms topics. A codec is used to encode a
 * message to a jms topic in order to publish a message. When a subscriber then receive the message the codes is
 * again used to decode the same message. A jms topic is mapped to one producer which is used to publish messages to
 * that topic. The jms topic is also mapped with a set of subscribers which are subscribed to that topic. A lock is
 * used to ensure subscribing, unsubscribing and publishing happens safely.
 *
 * @param <Topic> The type of topics that is allowed to identify jms topics.
 * @param <Subscriber> The type of subscribers that is allowed to subscribe to topics.
 * @param <Message> The type of messages that is allowed to be published to a jms topic.
 */
public class JmsBroker<Topic, Subscriber, Message> implements Broker<Topic, Subscriber, Message> {

    private final Connection connection;
    private final Session session;
    private final JmsCodec<Message> codec;

    private final Map<Topic, javax.jms.Topic> jmsTopics;
    private final AtomicInteger counter;

    private final Map<javax.jms.Topic, MessageProducer> producers;
    private final Map<javax.jms.Topic, Map<Subscriber, MessageConsumer>> subscribers;
    private final Lock lock;

    /**
     * Constructor for a JmsBroker, which starts a connection that has to be closed when the server closes.
     *
     * @param connection The connection used to create a session.
     * @param codec The needed coded for encoding and decoding messages.
     */
    public JmsBroker(Connection connection, JmsCodec<Message> codec) throws JMSException {

        this.connection = connection;
        this.connection.start();
        // The session acknowledges every message that is received automatically. Can handle both synchronous and
        // asynchronous message delivery. There is no need to manually acknowledge messages.
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.codec = codec;

        jmsTopics = new HashMap<>();
        counter = new AtomicInteger();

        producers = new HashMap<>();
        subscribers = new HashMap<>();
        lock = new ReentrantLock();
    }

    /**
     * Method used to convert a generic topic to a specific jms topic.
     *
     * @param topic A generic topic that is converted to a jms topic.
     * @return The jms topic corresponding with the provided generic topic.
     */
    private javax.jms.Topic convert(Topic topic) throws JMSException {

        javax.jms.Topic jmsTopic = jmsTopics.get(topic);

        if (jmsTopic == null) {
            jmsTopic = generate();
            jmsTopics.put(topic, jmsTopic);
        }

        return jmsTopic;
    }

    /**
     * Method for generating a new jms topic if a generic topic does not have a corresponding jms topic yet. A
     * counter is used to create a unique id for each topic.
     *
     * @return The new jms topic.
     */
    private javax.jms.Topic generate() throws JMSException {
        int id = counter.getAndIncrement();
        String name = String.valueOf(id);
        return session.createTopic(name);
    }

    /**
     * Create a new consumer for a jms topic. This consumer will receive messages that are published to the jms topic.
     * A message listener is added to automatically push the message to the subscriber using the provided callback.
     *
     * @param jmsTopic The topic from which to receive messages.
     * @param callback The callback that should be used to provide messages.
     * @return A messageConsumer for a specific jms topic.
     */
    private MessageConsumer createConsumer(javax.jms.Topic jmsTopic, Consumer<Message> callback) throws JMSException {
        MessageConsumer consumer = session.createConsumer(jmsTopic);
        consumer.setMessageListener(jmsMessage -> {
            try {
                Message message = codec.decode(session, jmsMessage);
                callback.accept(message);
            } catch (JMSException e) {
                throw new RuntimeException("Failed to decode JMS message", e);
            }
        });
        return consumer;
    }

    @Override
    public void subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback) {
        lock.lock();
        try {

            javax.jms.Topic jmsTopic = convert(topic);
            Map<Subscriber, MessageConsumer> consumers = subscribers.computeIfAbsent(jmsTopic, k -> new HashMap<>());

            if (consumers.isEmpty()) {
                MessageProducer producer = session.createProducer(jmsTopic);
                producers.put(jmsTopic, producer);
            }

            if (!consumers.containsKey(subscriber)) {
                MessageConsumer consumer = createConsumer(jmsTopic, callback);
                consumers.put(subscriber, consumer);
            }

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
            Map<Subscriber, MessageConsumer> consumers = subscribers.get(jmsTopic);

            if (consumers != null) {

                MessageConsumer consumer = consumers.remove(subscriber);
                if (consumer != null) {
                    consumer.setMessageListener(null);
                }

                if (consumers.isEmpty()) {
                    MessageProducer producer = producers.remove(jmsTopic);
                    if (producer != null) {
                        producer.close();
                    }
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
            MessageProducer producer = producers.get(jmsTopic);

            if (producer != null) {
                javax.jms.Message jmsMessage = codec.encode(session, message);
                producer.send(jmsMessage);
            }

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
            connection.close();
        } catch (JMSException e) {
            throw new RuntimeException("Failed to close JMS broker", e);
        }
    }
}
