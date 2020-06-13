package net.glowstone.messaging;

import com.flowpowered.network.Message;
import javax.jms.JMSException;
import net.glowstone.messaging.codecs.CompositeCodec;
import net.glowstone.util.config.BrokerConfig;
import net.glowstone.util.config.ChannelConfig;
import science.atlarge.opencraft.messaging.Broker;
import science.atlarge.opencraft.messaging.brokers.ActivemqBroker;
import science.atlarge.opencraft.messaging.brokers.ChannelFactory;
import science.atlarge.opencraft.messaging.brokers.ConcurrentBroker;
import science.atlarge.opencraft.messaging.brokers.RabbitmqBroker;
import science.atlarge.opencraft.messaging.brokers.ReadWriteBroker;
import science.atlarge.opencraft.messaging.channels.ConcurrentChannel;
import science.atlarge.opencraft.messaging.channels.GuavaChannel;
import science.atlarge.opencraft.messaging.channels.ReadWriteChannel;
import science.atlarge.opencraft.messaging.channels.UnsafeChannel;

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

            case "readwrite":
                return ReadWriteChannel::new;

            case "unsafe":
                return UnsafeChannel::new;

            default:
                throw new RuntimeException("Unknown channel type: " + type);
        }
    }

    private static <Topic, Subscriber> Broker<Topic, Subscriber, Message> newActivemqBroker(BrokerConfig config) {
        try {
            String host = config.getHost();
            int port = config.getPort();
            String username = config.getUsername();
            String password = config.getPassword();
            CompositeCodec codec = new CompositeCodec();
            return new ActivemqBroker<>(codec, host, port, username, password);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private static <Topic, Subscriber> Broker<Topic, Subscriber, Message> newRabbitmqBroker(BrokerConfig config) {
        try {
            String host = config.getHost();
            int port = config.getPort();
            String username = config.getUsername();
            String password = config.getPassword();
            String virtualHost = config.getVirtualHost();
            CompositeCodec codec = new CompositeCodec();
            return new RabbitmqBroker<>(codec, host, port, username, password, virtualHost);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The constructor is private to prevent the initialization of the factory class.
     */
    private Brokers() {}
}
