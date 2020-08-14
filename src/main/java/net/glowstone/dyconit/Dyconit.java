package net.glowstone.dyconit;

import com.google.common.collect.Maps;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.glowstone.messagetype.UpdateMessage;
import org.bukkit.entity.Player;

class Dyconit {
    Map<Player, Subscription> subscriptions;

    Dyconit(Player player) {
        subscriptions = Maps.newConcurrentMap();
        addSubscription(player);
    }

    static class Subscription {
        int stalenessBound;
        int numericalErrorBound;
        Instant timestampLastReset;
        final List<UpdateMessage> messageQueue;

        private Subscription() {
            stalenessBound = 100000;        //in milliseconds
            numericalErrorBound = 100;      //in amount of updates
            timestampLastReset = Instant.now();
            messageQueue = Collections.synchronizedList(new ArrayList<>());
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

    void addMessage(Player player, UpdateMessage message) {

        if (subscriptions.containsKey(player)) {
                subscriptions.get(player).messageQueue.add(message);
        }
    }
}
