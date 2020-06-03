package net.glowstone.messaging.brokers;

import net.glowstone.messaging.BrokerTest;
import net.glowstone.messaging.Subscriber;
import net.glowstone.messaging.channels.ReadWriteChannel;

final class ReadWriteBrokerTest extends BrokerTest {

    @Override
    protected ReadWriteBroker<String, Subscriber, String> createBroker() {
        return new ReadWriteBroker<>(ReadWriteChannel::new);
    }
}
