package net.glowstone.messaging.channels;

import net.glowstone.messaging.ChannelTest;
import net.glowstone.messaging.Subscriber;

public class UnsafeChannelTest extends ChannelTest {

    @Override
    protected UnsafeChannel<Subscriber, String> createChannel() {
        return new UnsafeChannel<>();
    }
}
