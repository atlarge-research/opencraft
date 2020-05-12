package net.glowstone.messaging.brokers;

import net.glowstone.messaging.Broker;
import net.glowstone.messaging.BrokerTest;
import net.glowstone.messaging.Subscriber;
import net.glowstone.messaging.brokers.ConcurrentBroker;

class ConcurrentBrokerTest extends BrokerTest {

    @Override
    protected Broker<String, Subscriber, String> createBroker() {
        return new ConcurrentBroker<>();
    }
}
