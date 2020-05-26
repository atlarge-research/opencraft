package net.glowstone.messaging.brokers;

import net.glowstone.messaging.Channel;

/**
 * The channelFactory provides an object that can be used to create channels.
 *
 * @param <Subscriber> the type of subscribers that is allowed to subscribe to the channel.
 * @param <Message> the type of messages that is allowed to be published to the channel.
 */
@FunctionalInterface
public interface ChannelFactory<Subscriber, Message> {

    /**
     * Creates a channel and returns it.
     * @return a new channel
     */
    Channel<Subscriber, Message> create();
}
