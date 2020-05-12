package net.glowstone.messaging.brokers;

import net.glowstone.messaging.ChannelTest;
import net.glowstone.messaging.Subscriber;
import net.glowstone.messaging.brokers.ConcurrentChannel;

class ConcurrentChannelTest extends ChannelTest {

    @Override
    protected ConcurrentChannel<Subscriber, String> createChannel() {
        return new ConcurrentChannel<>();
    }
}
