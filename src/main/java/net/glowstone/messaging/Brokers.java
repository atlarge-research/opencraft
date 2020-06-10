package net.glowstone.messaging;

import com.flowpowered.network.Message;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import net.glowstone.messaging.brokers.ChannelFactory;
import net.glowstone.messaging.brokers.ConcurrentBroker;
import net.glowstone.messaging.brokers.JmsBroker;
import net.glowstone.messaging.brokers.ReadWriteBroker;
import net.glowstone.messaging.brokers.codecs.CompositeCodec;
import net.glowstone.messaging.channels.ConcurrentChannel;
import net.glowstone.messaging.channels.GuavaChannel;
import net.glowstone.messaging.channels.ReadWriteChannel;
import net.glowstone.messaging.channels.UnsafeChannel;
import net.glowstone.util.config.BrokerConfig;
import net.glowstone.util.config.ChannelConfig;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * A factory class to for creating multiple types of brokers.
 */
public final class Brokers {

    /**
     * Create a broker based on the given configuration.
     *
     * @param config the configuration to be used.
     * @param <Topic> the type of topics that is allowed to identify channels.
     * @param <Subscriber> the type of subscribers that is allowed to subscribe to a channel.
     * @return the created broker.
     */
    public static <Topic, Subscriber> Broker<Topic, Subscriber, Message> newBroker(BrokerConfig config) {
        String type = config.getType();
        switch (type.toLowerCase()) {

            case "activemq":
                return newActivemqBroker(config);

            case "concurrent":
                return newConcurrentBroker(config.getChannel());

            case "rabbitmq":
                return newRabbitmqBroker(config);

            case "readwrite":
                return newReadWriteBroker(config.getChannel());

            default:
                throw new RuntimeException("Unknown broker type: " + type);
        }
    }

    private static <Topic, Subscriber> Broker<Topic, Subscriber, Message> newConcurrentBroker(ChannelConfig config) {
        ChannelFactory<Subscriber, Message> factory = newChannelFactory(config);
        return new ConcurrentBroker<>(factory);
    }

    private static <Topic, Subscriber> Broker<Topic, Subscriber, Message> newReadWriteBroker(ChannelConfig config) {
        ChannelFactory<Subscriber, Message> factory = newChannelFactory(config);
        return new ReadWriteBroker<>(factory);
    }

    private static <Subscriber> ChannelFactory<Subscriber, Message> newChannelFactory(ChannelConfig config) {
        String type = config.getType();
        switch (type.toLowerCase()) {

            case "concurrent":
                int parallelismThreshold = config.getParallelismThreshold();
                return () -> new ConcurrentChannel<>(parallelismThreshold);

            case "guava":
                return GuavaChannel::new;

            case "readWrite":
                return ReadWriteChannel::new;

            case "unsafe":
                return UnsafeChannel::new;

            default:
                throw new RuntimeException("Unknown channel type: " + type);
        }
    }

    private static <Topic, Subscriber> Broker<Topic, Subscriber, Message> newActivemqBroker(BrokerConfig config) {
        try {
            String url = "tcp://" + config.getHost() + ":" + config.getPort();
            ConnectionFactory factory = new ActiveMQConnectionFactory(url);
            Connection connection = factory.createConnection(config.getUsername(), config.getPassword());
            return newJmsBroker(connection);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private static <Topic, Subscriber> Broker<Topic, Subscriber, Message> newRabbitmqBroker(BrokerConfig config) {
        try {
            RMQConnectionFactory factory = new RMQConnectionFactory();
            factory.setHost(config.getHost());
            factory.setPort(config.getPort());
            factory.setVirtualHost(config.getVirtualHost());
            Connection connection = factory.createTopicConnection(config.getUsername(), config.getPassword());
            return newJmsBroker(connection);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private static <Topic, Subscriber> Broker<Topic, Subscriber, Message> newJmsBroker(Connection connection) {
        try {
            CompositeCodec codec = new CompositeCodec();
            return new JmsBroker<>(connection, codec);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The constructor is private to prevent the initialization of the factory class.
     */
    private Brokers() {}
}
