package net.glowstone.messaging.brokers.jms;

import java.util.Objects;
import javax.jms.MessageConsumer;
import lombok.Getter;

/**
 * The subscriber used by a jms broker. The subscriber is connected to a message consumer. These message consumers
 * can be seen as a subscriber for one specific topic.
 *
 * @param <Subscriber> The type of subscribers that is allowed to subscribe to topics.
 */
final class JmsSubscriber<Subscriber> {

    private final MessageConsumer consumer;
    @Getter
    private final Subscriber subscriber;

    /**
     * Create a jms subscriber.
     *
     * @param consumer A consumer created by jms for a specific topic.
     * @param subscriber The subscriber connected to the consumer.
     */
    JmsSubscriber(MessageConsumer consumer, Subscriber subscriber) {
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
