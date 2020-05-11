package net.glowstone.messaging.concurrent;

import net.glowstone.messaging.Broker;
import net.glowstone.messaging.BrokerTest;
import net.glowstone.messaging.Subscriber;

class ConcurrentBrokerTest extends BrokerTest {

    @Override
    protected Broker<String, Subscriber, String> createBroker() {
        return new ConcurrentBroker<>();
    }
}
