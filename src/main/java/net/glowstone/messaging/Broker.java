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

    // TODO: Add exists(Topic topic) method.

    // TODO: Add isSubscribed(Topic topic, Subscriber subscriber) method.

    /**
     * Registers the given subscriber to receive messages of the given topic via the given callback.
     *
     * @param topic the topic in which the subscriber is interested.
     * @param subscriber the subscriber that would like to receive messages.
     * @param callback the callback that should be used to provide messages.
     */
    void subscribe(Topic topic, Subscriber subscriber, Consumer<Message> callback);

    /**
     * Unregisters the given subscriber from receiving messages of the given topic.
     *
     * @param topic the topic in which the subscriber is no longer interested.
     * @param subscriber the subscriber that would no longer like to receive messages.
     */
    void unsubscribe(Topic topic, Subscriber subscriber);

    /**
     * Broadcasts the given message to all subscribers of the given topic.
     *
     * @param topic the topic of the given message.
     * @param message the message to be published.
     */
    void publish(Topic topic, Message message);
}
