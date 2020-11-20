package science.atlarge.opencraft.opencraft.messaging.dyconits.policies;

import com.flowpowered.network.Message;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import science.atlarge.opencraft.dyconits.Bounds;
import science.atlarge.opencraft.dyconits.MessageChannel;
import science.atlarge.opencraft.dyconits.Subscriber;
import science.atlarge.opencraft.dyconits.policies.DyconitCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitPolicy;
import science.atlarge.opencraft.dyconits.policies.DyconitSubscribeCommand;

public class DonnybrookPolicy implements DyconitPolicy<Player, Message> {

    private static final String CATCH_ALL_DYCONIT_NAME = "catch-all";
    private static final int INTEREST_SET_SIZE = 5;

    /**
     * 1/3.4 seconds. This value is from a user experiment in the Donnybrook paper.
     * Here they find that user's interest sets changes 3.4 times per second.
     */
    private final Duration turnoverTime = Duration.ofNanos(294117);

    private final Map<Subscriber<Player, Message>, Instant> lastChangedMap = new HashMap<>();
    private final Map<Subscriber<Player, Message>, List<Player>> interestSet = new HashMap<>();
    private final Bounds oneSecondBound = new Bounds(1000, -1);
    private final Random random = new Random(System.currentTimeMillis());

    @NotNull
    @Override
    public String computeAffectedDyconit(@NotNull Object publisher) {
        if (publisher instanceof Player) {
            return entityToName((Player) publisher);
        }
        return CATCH_ALL_DYCONIT_NAME;
    }

    private String entityToName(Player player) {
        return player.getName();
    }

    @NotNull
    @Override
    public List<DyconitCommand<Player, Message>> update(@NotNull Subscriber<Player, Message> subscriber) {
        Instant lastChanged = lastChangedMap.computeIfAbsent(subscriber, p -> Instant.MIN);
        List<DyconitCommand<Player, Message>> commands = new ArrayList<>();
        Player thisPlayer = subscriber.getKey();
        MessageChannel<Message> thisCallback = subscriber.getCallback();
        commands.add(new DyconitSubscribeCommand<>(thisPlayer, thisCallback, Bounds.Companion.getZERO(), CATCH_ALL_DYCONIT_NAME));
        // We update players' interest sets after turnoverTime has passed. See documentation at variable declaration.
        if (turnoverTime.minus(Duration.between(lastChanged, Instant.now())).isNegative()) {
            Set<Player> allPlayers = new HashSet<>(thisPlayer.getWorld().getPlayers());
            allPlayers.remove(thisPlayer);
            if (allPlayers.size() > 0) {
                List<Player> playerSubscriberSet = interestSet.computeIfAbsent(subscriber, s -> new ArrayList<>());
                List<Player> candidates = allPlayers.stream().filter(p -> !playerSubscriberSet.contains(p)).collect(Collectors.toList());
                // We randomly select which players to add and remove from the interest set.
                // This is different from Donnybrook's approach, which calculates an attention score.
                // Because we think the difference in bandwidth usage is negligible, we use our own, simpler, approach.
                if (playerSubscriberSet.size() >= INTEREST_SET_SIZE && candidates.size() > 0) {
                    playerSubscriberSet.remove(random.nextInt(playerSubscriberSet.size()));
                }
                playerSubscriberSet.add(candidates.get(random.nextInt(candidates.size())));
                // We set zero bounds for players in our interest set (send immediately).
                // For all other players, we send updates once per second.
                playerSubscriberSet
                        .forEach(p -> commands.add(new DyconitSubscribeCommand<>(thisPlayer, thisCallback, Bounds.Companion.getZERO(), entityToName(p))));
                allPlayers.stream()
                        .filter(p -> !playerSubscriberSet.contains(p))
                        .forEach(p -> commands.add(new DyconitSubscribeCommand<>(thisPlayer, thisCallback, oneSecondBound, entityToName(p))));
                lastChangedMap.put(subscriber, Instant.now());
            }
        }
        return commands;
    }

    @Override
    public int weigh(Message message) {
        return 1;
    }
}
