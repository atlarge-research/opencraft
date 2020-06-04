package net.glowstone.messaging;

import com.flowpowered.network.Message;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import net.glowstone.messaging.brokers.ChannelFactory;
import net.glowstone.messaging.brokers.ConcurrentBroker;
import net.glowstone.messaging.brokers.JmsBroker;
import net.glowstone.messaging.brokers.JmsCodec;
import net.glowstone.messaging.brokers.ReadWriteBroker;
import net.glowstone.messaging.brokers.codecs.ProtocolCodec;
import net.glowstone.messaging.channels.ConcurrentChannel;
import net.glowstone.messaging.channels.GuavaChannel;
import net.glowstone.messaging.channels.ReadWriteChannel;
import net.glowstone.messaging.channels.UnsafeChannel;
import net.glowstone.net.protocol.GlowProtocol;
import net.glowstone.net.protocol.PlayProtocol;
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
     * @param <T> the type of topics that is allowed to identify channels.
     * @param <S> the type of subscribers that is allowed to subscribe to a channel.
     * @param <M> the type of messages that is allowed to be published to a channel.
     * @return the created broker.
     */
    public static <T, S, M extends Message> Broker<T, S, M> newBroker(BrokerConfig config) {
        String type = config.getType();
        switch (type) {

            case "ActiveMQ":
                return newActivemqBroker(config);

            case "Concurrent":
                return newConcurrentBroker(config.getChannel());

            case "RabbitMQ":
                return newRabbitmqBroker(config);

            case "ReadWrite":
                return newReadWriteBroker(config.getChannel());

            default:
                throw new RuntimeException("Unknown broker type: " + type);
        }
    }

    private static <T, S, M> Broker<T, S, M> newConcurrentBroker(ChannelConfig config) {
        ChannelFactory<S, M> factory = newChannelFactory(config);
        return new ConcurrentBroker<>(factory);
    }

    private static <T, S, M> Broker<T, S, M> newReadWriteBroker(ChannelConfig config) {
        ChannelFactory<S, M> factory = newChannelFactory(config);
        return new ReadWriteBroker<>(factory);
    }

    private static <S, M> ChannelFactory<S, M> newChannelFactory(ChannelConfig config) {
        String type = config.getType();
        switch (type) {

            case "Concurrent":
                int parallelismThreshold = config.getParallelismThreshold();
                return () -> new ConcurrentChannel<>(parallelismThreshold);

            case "Guava":
                return GuavaChannel::new;

            case "ReadWrite":
                return ReadWriteChannel::new;

            case "Unsafe":
                return UnsafeChannel::new;

            default:
                throw new RuntimeException("Unknown channel type: " + type);
        }
    }

    private static <T, S, M extends Message> Broker<T, S, M> newActivemqBroker(BrokerConfig config) {
        try {
            String url = "tcp://" + config.getHost() + ":" + config.getPort();
            ConnectionFactory factory = new ActiveMQConnectionFactory(url);
            Connection connection = factory.createConnection(config.getUsername(), config.getPassword());
            return newJmsBroker(connection);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T, S, M extends Message> Broker<T, S, M> newRabbitmqBroker(BrokerConfig config) {
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

    private static <T, S, M extends Message> Broker<T, S, M> newJmsBroker(Connection connection) {
        try {
            GlowProtocol protocol = new PlayProtocol();
            JmsCodec<M> codec = new ProtocolCodec<>(protocol);
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
