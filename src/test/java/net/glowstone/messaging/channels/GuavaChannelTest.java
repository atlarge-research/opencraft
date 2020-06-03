package net.glowstone.messaging.channels;

import net.glowstone.messaging.ChannelTest;
import net.glowstone.messaging.Subscriber;

public class GuavaChannelTest extends ChannelTest {

    @Override
    protected GuavaChannel<Subscriber, String> createChannel() {
        return new GuavaChannel<>();
    }
}
