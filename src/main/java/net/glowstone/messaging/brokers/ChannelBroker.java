package net.glowstone.messaging.brokers;

import net.glowstone.messaging.Broker;
import net.glowstone.messaging.Channel;

/**
 * The Channel broker uses a concurrent hash map to store topic-channel pairs. The concurrent
 * hash map allows multiple publishers and subscribers to access the broker simultaneously.
 *
 * @param <Topic> the type of topics that is allowed to identify channels.
 * @param <Subscriber> the type of subscribers that is allowed to subscribe to a channel.
 * @param <Message> the type of messages that is allowed to be published to a channel.
 */
public abstract class ChannelBroker<Topic, Subscriber, Message> implements Broker<Topic, Subscriber, Message> {

    private final ChannelFactory<Subscriber, Message> channelFactory;

    /**
     * Create a concurrent broker.
     */
    public ChannelBroker(ChannelFactory<Subscriber, Message> channelFactory) {
        this.channelFactory = channelFactory;
    }

    /**
     * Creates a channel that corresponds to the channel interface.
     * @return A channel
     */
    protected Channel<Subscriber, Message> createChannel() {
        return channelFactory.create();
    }

}
