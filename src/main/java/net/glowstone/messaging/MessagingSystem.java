package net.glowstone.messaging;

import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * The messaging system provides a generalised interface for subscription management and message routing.
 *
 * @param <Topic> the type of topics used by the messaging system.
 * @param <Subscriber> the type of subscribers that can receive published messages.
 * @param <Publisher> the type of publisher that can publish messages.
 * @param <Message> the type of messages that can be published.
 */
public final class MessagingSystem<Topic, Subscriber, Publisher, Message> {

    private final Policy<Topic, Subscriber, Publisher> policy;
    private final Broker<Topic, Subscriber, Message> broker;
    private final Map<Subscriber, Set<Topic>> subscriptions;

    /**
     * Create a messaging system based on the given policy and broker.
     *
     * @param policy the policy used to update subscriptions and select publishing targets.
     * @param broker the broker used to distribute messages to subscribers.
     */
    public MessagingSystem(Policy<Topic, Subscriber, Publisher> policy, Broker<Topic, Subscriber, Message> broker) {
        this.policy = policy;
        this.broker = broker;
        subscriptions = new HashMap<>();
    }

    /**
     * Update the subscriptions of the subscriber and register its callback.
     *
     * @param subscriber the subscriber whom's subscriptions should be updated.
     * @param callback the callback that should be used to send messages to the subscriber.
     */
    public void update(Subscriber subscriber, Consumer<Message> callback) {

        Set<Topic> newTopics = policy.computeInterestSet(subscriber);

        if (newTopics.isEmpty()) {

            Set<Topic> oldTopics = subscriptions.remove(subscriber);
            oldTopics.forEach(topic -> broker.unsubscribe(topic, subscriber));

        } else {

            Set<Topic> oldTopics = subscriptions.put(subscriber, newTopics);
            if (oldTopics == null) {
                newTopics.forEach(topic -> broker.subscribe(topic, subscriber, callback));
            } else {
                Sets.difference(oldTopics, newTopics).forEach(topic -> broker.unsubscribe(topic, subscriber));
                Sets.difference(newTopics, oldTopics).forEach(topic -> broker.subscribe(topic, subscriber, callback));
            }
        }
    }

    /**
     * Determine the topic to which the publisher should publish its message and then publish it.
     *
     * @param publisher the publisher who would like to publish a message.
     * @param message the message to be published.
     */
    public void broadcast(Publisher publisher, Message message) {
        Topic topic = policy.selectTarget(publisher);
        broker.publish(topic, message);
    }
}
