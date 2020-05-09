package net.glowstone.messaging.concurrent;

import net.glowstone.messaging.ChannelTest;
import net.glowstone.messaging.Subscriber;

public class ConcurrentChannelTest extends ChannelTest {

    @Override
    protected ConcurrentChannel<Subscriber, String> createChannel() {
        return new ConcurrentChannel<>();
    }
}
