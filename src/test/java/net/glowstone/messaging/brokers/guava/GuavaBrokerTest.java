package net.glowstone.messaging.brokers.guava;

import net.glowstone.messaging.BrokerTest;
import net.glowstone.messaging.Subscriber;

public class GuavaBrokerTest extends BrokerTest {

    @Override
    protected GuavaBroker<String, Subscriber, String> createBroker() {
        return new GuavaBroker<>();
    }
}
