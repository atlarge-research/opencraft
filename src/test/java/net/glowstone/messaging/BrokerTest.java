package net.glowstone.messaging;

public abstract class BrokerTest {

    protected abstract Broker<String, Subscriber, String> createBroker();
}
