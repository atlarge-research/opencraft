package net.glowstone.messaging;

import java.util.Set;

/**
 * The policy interface provides methods which describe how subscribers and publishers relate to a set of topics.
 *
 * @param <Topic> the type of topics that could be of interest.
 * @param <Publisher> the type of publishers that have a target topic.
 * @param <Subscriber> the type of subscribers that have topics of interest.
 */
public interface Policy<Topic, Publisher, Subscriber> {

    /**
     * Compute the topics a subscriber is interested in.
     *
     * @param subscriber the subscriber who's topics of interest should be computed.
     * @return the topics of interest.
     */
    Set<Topic> computeInterestSet(Subscriber subscriber);

    /**
     * Select the topics to which the publisher should publish its message.
     *
     * @param publisher the publisher who's topics to select.
     * @return the selected topics.
     */
    Iterable<Topic> selectTargets(Publisher publisher);
}
