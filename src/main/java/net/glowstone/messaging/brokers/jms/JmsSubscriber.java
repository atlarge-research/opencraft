package net.glowstone.messaging.brokers.jms;

import java.util.Objects;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;

final class JmsSubscriber<Subscriber> {

    private final MessageConsumer consumer;
    private final Subscriber subscriber;

    JmsSubscriber(MessageConsumer consumer, Subscriber subscriber) throws JMSException {
        this.consumer = consumer;
        this.subscriber = subscriber;
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        JmsSubscriber<?> that = (JmsSubscriber<?>) other;
        return subscriber.equals(that.subscriber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriber);
    }
}
