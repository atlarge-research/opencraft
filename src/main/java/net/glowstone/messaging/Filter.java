package net.glowstone.messaging;

/**
 * The filter interface describes which subscribers should receive which messages broadcast via the messaging system.
 *
 * @param <Subscriber> the type of subscribers who could receive messages.
 * @param <Message> the type of messages that could be received.
 */
@FunctionalInterface
public interface Filter<Subscriber, Message> {

    /**
     * Determine whether the subscriber should receive the message.
     *
     * @param subscriber the subscriber who could receive the message.
     * @param message the message that could be received.
     * @return whether the subscriber should receive the message.
     */
    boolean filter(Subscriber subscriber, Message message);
}
