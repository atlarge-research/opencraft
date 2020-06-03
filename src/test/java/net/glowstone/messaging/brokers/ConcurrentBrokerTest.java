package net.glowstone.messaging.brokers;

import net.glowstone.messaging.BrokerTest;
import net.glowstone.messaging.Subscriber;
import net.glowstone.messaging.channels.ConcurrentChannel;

final class ConcurrentBrokerTest extends BrokerTest {

    @Override
    protected ConcurrentBroker<String, Subscriber, String> createBroker() {
        return new ConcurrentBroker<>(ConcurrentChannel::new);
    }
}
