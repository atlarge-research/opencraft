package net.glowstone.dyconit;

import com.flowpowered.network.Message;
import com.google.common.collect.Maps;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import org.bukkit.entity.Player;

class Dyconit {
    Map<Player, Subscription> subscriptions;

    Dyconit(Player player) {
        subscriptions = Maps.newHashMap();
        addSubscription(player);
    }

    static class Subscription {
        int stalenessBound;
        int numericalErrorBound;
        Instant timestampLastReset;
        ArrayList<Message> messageQueue;

        private Subscription() {
            stalenessBound = 100;           //in milliseconds
            numericalErrorBound = 100;      //in amount of updates
            timestampLastReset = Instant.now();
            messageQueue = new ArrayList<>();
        }

        boolean exceedBound() {
            //TODO: check for numerical error bound
            if (Duration.between(timestampLastReset, Instant.now()).toMillis() > stalenessBound) {
                timestampLastReset = Instant.now();
                return true;
            }
            return false;
        }

    }

    void addSubscription(Player player) {
        subscriptions.putIfAbsent(player, new Subscription());
    }

    void addMessage(Player player, Message message) {
        if (subscriptions.containsKey(player)) {
            subscriptions.get(player).messageQueue.add(message);
        }
    }
}
