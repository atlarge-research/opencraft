package net.glowstone.messaging.channels;

import net.glowstone.messaging.ChannelTest;
import net.glowstone.messaging.Subscriber;

public class ReadWriteChannelTest extends ChannelTest {

    @Override
    protected ReadWriteChannel<Subscriber, String> createChannel() {
        return new ReadWriteChannel<>();
    }
}
