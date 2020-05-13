package net.glowstone.messaging;

import java.util.Set;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class MessagingSystemTest {

    private class TestPolicy implements Policy<String, Subscriber, Subscriber> {

        @Override
        public Set<String> computeInterestSet(Subscriber subscriber) {
            return null;
        }

        @Override
        public String selectTarget(Subscriber subscriber) {
            return subscriber.getName();
        }
    }

    private class TestBroker implements Broker<String, Subscriber, String> {

        @Override
        public void subscribe(String s, Subscriber subscriber, Consumer<String> callback) {

        }

        @Override
        public void unsubscribe(String s, Subscriber subscriber) {

        }

        @Override
        public void publish(String s, String s2) {

        }
    }

    private MessagingSystem<String, Subscriber, Subscriber, String> messagingSystem;

    @BeforeEach
    void beforeEach() {
        Policy<String, Subscriber, Subscriber> policy = new TestPolicy();
        Broker<String, Subscriber, String> broker = new TestBroker();
        messagingSystem = new MessagingSystem<>(policy, broker);
    }

    @Test
    void noInterestsTest() {
        Subscriber alice = new Subscriber("Alice");
        messagingSystem.update(alice, alice::onMessage);
    }

    @Test
    void noNewInterestsTest() {

    }

    @Test
    void noOldInterestsTest() {

    }

    @Test
    void updateInterestsTest() {

    }

    @Test
    void broadcastTest() {

    }
}