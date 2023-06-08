package science.atlarge.opencraft.opencraft.messaging.dyconits.policies;

import com.flowpowered.network.Message;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import science.atlarge.opencraft.dyconits.Bounds;
import science.atlarge.opencraft.dyconits.Subscriber;
import science.atlarge.opencraft.dyconits.policies.DyconitCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitPolicy;
import science.atlarge.opencraft.dyconits.policies.DyconitSubscribeCommand;
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.messaging.dyconits.policies.weights.DistanceMoved;
import science.atlarge.opencraft.opencraft.messaging.dyconits.policies.weights.WeighMessage;

public class InterestSet implements DyconitPolicy<Player, Message> {

    private static final String CATCH_ALL_DYCONIT_NAME = "catch-all";
    private static final int INTEREST_SET_SIZE = 5;

    /**
     * 1/3.4 seconds. This value is from a user experiment in the Donnybrook paper.
     * Here they find that user's interest sets changes 3.4 times per second.
     */
    private final Duration turnoverTime = Duration.ofNanos(294117647);

    private final Map<Subscriber<Player, Message>, Instant> lastChangedMap = new HashMap<>();
    private final Map<Subscriber<Player, Message>, List<Player>> interestSet = new HashMap<>();
    private final Bounds oneSecondBound = new Bounds(1000, -1);
    private final Random random = new Random(System.currentTimeMillis());

    private final WeighMessage weighMessage;

    public InterestSet(GlowServer server) {
        weighMessage = new DistanceMoved(server, new Bounds(-1, 5));
    }

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
        // Make sure not all players have the same lastChangedTime, to
        Instant lastChanged = lastChangedMap.computeIfAbsent(subscriber, p -> Instant.ofEpochMilli(System.currentTimeMillis() - ThreadLocalRandom.current().nextLong(turnoverTime.toMillis())));
        List<DyconitCommand<Player, Message>> commands = new ArrayList<>();
        Player thisPlayer = subscriber.getKey();
        java.util.function.Consumer<Message> callback = subscriber.getCallback();
        commands.add(new DyconitSubscribeCommand<>(thisPlayer, thisCallback, Bounds.Companion.getZERO(), CATCH_ALL_DYCONIT_NAME));
        // We update players' interest sets after turnoverTime has passed. See documentation at variable declaration.
        if (!interestSet.containsKey(subscriber)) {
            interestSet.put(subscriber, new ArrayList<>());
            thisPlayer.getWorld().getPlayers().stream()
                    .filter(p -> !p.equals(thisPlayer))
                    .map(p -> new DyconitSubscribeCommand<>(thisPlayer, thisCallback, oneSecondBound, entityToName(p)))
                    .forEach(commands::add);
        } else if (turnoverTime.minus(Duration.between(lastChanged, Instant.now())).isNegative()) {
            Set<Player> allPlayers = new HashSet<>(thisPlayer.getWorld().getPlayers());
            allPlayers.remove(thisPlayer);
            if (allPlayers.size() > 0) {
                List<Player> playerSubscriberSet = interestSet.computeIfAbsent(subscriber, s -> new ArrayList<>());
                List<Player> candidates = allPlayers.stream().filter(p -> !playerSubscriberSet.contains(p)).collect(Collectors.toList());
                // We randomly select which players to add and remove from the interest set.
                // This is different from Donnybrook's approach, which calculates an attention score.
                // Because we think the difference in bandwidth usage is negligible, we use our own, simpler, approach.
                if (candidates.size() > 0) {
                    if (playerSubscriberSet.size() >= INTEREST_SET_SIZE) {
                        Player removed = playerSubscriberSet.remove(random.nextInt(playerSubscriberSet.size()));
                        commands.add(new DyconitSubscribeCommand<>(thisPlayer, thisCallback, oneSecondBound, entityToName(removed)));
                    }

                    Player added = candidates.get(random.nextInt(candidates.size()));
                    commands.add(new DyconitSubscribeCommand<>(thisPlayer, thisCallback, Bounds.Companion.getZERO(), entityToName(added)));
                    playerSubscriberSet.add(added);
                }
                lastChangedMap.put(subscriber, Instant.now());
            }
        }
        return commands;
    }

    @Override
    public int weigh(Message message) {
        return weighMessage.weigh(message);
    }

    @NotNull
    @Override
    public List<DyconitCommand<Player, Message>> globalUpdate() {
        return Collections.emptyList();
    }
}