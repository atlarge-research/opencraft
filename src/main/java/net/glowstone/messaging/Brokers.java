package net.glowstone.messaging;

import java.util.function.Supplier;
import javax.jms.JMSException;
import net.glowstone.util.config.BrokerConfig;
import science.atlarge.opencraft.messaging.Broker;
import science.atlarge.opencraft.messaging.brokers.ActivemqBroker;
import science.atlarge.opencraft.messaging.brokers.AsyncBroker;
import science.atlarge.opencraft.messaging.brokers.ChannelFactory;
import science.atlarge.opencraft.messaging.brokers.ConcurrentBroker;
import science.atlarge.opencraft.messaging.brokers.JmsCodec;
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
    public static <Topic, Subscriber, Message> Broker<Topic, Subscriber, Message> newBroker(
            BrokerConfig config,
            Supplier<JmsCodec<Message>> codecFactory
    ) {
        Broker<Topic, Subscriber, Message> base = newBaseBroker(config, codecFactory);
        if (config.getAsync()) {
            return new AsyncBroker<>(base, config.getThreads(), config.getCapacity());
        }
        return base;
    }

    private static <Topic, Subscriber, Message> Broker<Topic, Subscriber, Message> newBaseBroker(
            BrokerConfig config,
            Supplier<JmsCodec<Message>> codecFactory
    ) {
        switch (config.getType()) {

            case ACTIVEMQ:
                return newActivemqBroker(config, codecFactory);

            case CONCURRENT:
                return newConcurrentBroker(config);

            case RABBITMQ:
                return newRabbitmqBroker(config, codecFactory);

            case READ_WRITE:
                return newReadWriteBroker(config);

            default:
                throw new RuntimeException("Unsupported broker type: " + config.getType());
        }
    }

    private static <Topic, Subscriber, Message> Broker<Topic, Subscriber, Message> newConcurrentBroker(
            BrokerConfig config
    ) {
        ChannelFactory<Subscriber, Message> factory = newChannelFactory(config);
        return new ConcurrentBroker<>(factory);
    }

    private static <Topic, Subscriber, Message> Broker<Topic, Subscriber, Message> newReadWriteBroker(
            BrokerConfig config
    ) {
        ChannelFactory<Subscriber, Message> factory = newChannelFactory(config);
        return new ReadWriteBroker<>(factory);
    }

    private static <Subscriber, Message> ChannelFactory<Subscriber, Message> newChannelFactory(BrokerConfig config) {
        switch (config.getChannel()) {

            case CONCURRENT:
                return ConcurrentChannel::new;

            case GUAVA:
                return GuavaChannel::new;

            case READ_WRITE:
                return ReadWriteChannel::new;

            case UNSAFE:
                return UnsafeChannel::new;

            default:
                throw new RuntimeException("Unsupported channel type: " + config.getChannel());
        }
    }

    private static <Topic, Subscriber, Message> Broker<Topic, Subscriber, Message> newActivemqBroker(
            BrokerConfig config,
            Supplier<JmsCodec<Message>> codecFactory
    ) {
        try {
            JmsCodec<Message> codec = codecFactory.get();
            String host = config.getHost();
            int port = config.getPort();
            String username = config.getUsername();
            String password = config.getPassword();
            return new ActivemqBroker<>(codec, host, port, username, password);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private static <Topic, Subscriber, Message> Broker<Topic, Subscriber, Message> newRabbitmqBroker(
            BrokerConfig config,
            Supplier<JmsCodec<Message>> codecFactory
    ) {
        try {
            JmsCodec<Message> codec = codecFactory.get();
            String host = config.getHost();
            int port = config.getPort();
            String username = config.getUsername();
            String password = config.getPassword();
            String virtualHost = config.getVirtualHost();
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
