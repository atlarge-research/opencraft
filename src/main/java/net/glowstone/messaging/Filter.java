package net.glowstone.messaging;

@FunctionalInterface
public interface Filter<Subscriber, Message> {

    boolean filter(Subscriber subscriber, Message message);
}
