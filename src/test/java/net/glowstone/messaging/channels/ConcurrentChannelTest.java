package net.glowstone.messaging.channels;

import net.glowstone.messaging.ChannelTest;
import net.glowstone.messaging.Subscriber;

final class ConcurrentChannelTest extends ChannelTest {

    @Override
    protected ConcurrentChannel<Subscriber, String> createChannel() {
        return new ConcurrentChannel<>(4);
    }
}
