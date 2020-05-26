package net.glowstone.messaging.brokers;

import net.glowstone.messaging.Channel;

@FunctionalInterface
public interface ChannelFactory<Subscriber, Message> {
    /**
     * Creates a channel and returns it.
     * @return a new channel
     */
    Channel<Subscriber, Message> create();
}
