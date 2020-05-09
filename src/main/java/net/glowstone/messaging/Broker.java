package net.glowstone.messaging;

import java.util.function.Consumer;

/**
 * The Broker interface defines the methods to be implemented by a class,
 * such that it can function as broker in the pub/sub design pattern.
 *
 * @param <Topic> the type of topics that is allowed to identify channels.
 * @param <Subscriber> the type of subscribers that is allowed to subscribe to a channel.
 * @param <Message> the type of messages that is allowed to be published to a channel.
 */
public interface Broker<Topic, Subscriber, Message> {

    /**
     * Check whether the broker is empty, meaning that there are no subscribers.
     *
     * @return whether there are any subscribers.
     */
    boolean isEmpty();

    /**
     * Check whether a subscriber is subscribed to the topic.
     *
     * @param topic the topic of interest.
     * @param subscriber the subscriber whom's subscription should be checked.
     * @return whether the subscriber is subscribed to the topic.
     */
    boolean isSubscribed(Topic topic, Subscriber subscriber);

    /**
     * Register the subscriber to receive messages of the given topic via the callback.
     * Do not update the value if the subscriber is already subscribed.
     *
     *
     * @param topic the topic in which the subscriber is interested.
     * @param subscriber the subscriber that would like to receive messages.
     * @param callback the callback that should be used to provide messages.
     * @return whether the subscriber was previously unsubscribed.
     */
    boolean subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback);

    /**
     * Unregister the subscriber from receiving messages of the given topic.
     *
     * @param topic the topic in which the subscriber is no longer interested.
     * @param subscriber the subscriber that would no longer like to receive messages.
     * @return whether the subscriber was previously subscribed.
     */
    boolean unsubscribe(Topic topic, Subscriber subscriber);

    /**
     * Broadcast the given message to all subscribers of the given topic.
     *
     * @param topic the topic of the given message.
     * @param message the message to be published to subscribers.
     */
    void publish(Topic topic, Message message);
}
