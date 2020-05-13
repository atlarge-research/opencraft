package net.glowstone.messaging.brokers.concurrent;

import net.glowstone.messaging.BrokerTest;
import net.glowstone.messaging.Subscriber;

final class ConcurrentBrokerTest extends BrokerTest {

    @Override
    protected ConcurrentBroker<String, Subscriber, String> createBroker() {
        return new ConcurrentBroker<>();
    }
}
