package science.atlarge.opencraft.opencraft.messaging.dyconits.policies;

import com.flowpowered.network.Message;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.messaging.dyconits.policies.weights.DistanceMoved;
import science.atlarge.opencraft.opencraft.messaging.dyconits.policies.weights.WeighMessage;

public class PlayerPolicy implements DyconitPolicy<Player, Message> {

    private static final String CATCH_ALL_DYCONIT_NAME = "catch-all";

    private final Set<Subscriber<Player, Message>> subscriberSet = new HashSet<>();
    private final Bounds bound;
    private final WeighMessage weighMessage;

    public PlayerPolicy(GlowServer server, Bounds bound) {
        weighMessage = new DistanceMoved(server, bound);
        this.bound = bound;
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
        Player player = subscriber.getKey();
        MessageChannel<Message> callback = subscriber.getCallback();
        if (!subscriberSet.contains(subscriber)) {
            List<DyconitCommand<Player, Message>> commands = subscriberSet.stream()
                    .map(Subscriber::getKey)
                    .map(p -> new DyconitSubscribeCommand<>(player, callback, bound, entityToName(p)))
                    .collect(Collectors.toList());
            commands.addAll(subscriberSet.stream()
                    .map(s -> new DyconitSubscribeCommand<>(s.getKey(), s.getCallback(), bound, entityToName(player)))
                    .collect(Collectors.toList()));
            subscriberSet.add(subscriber);
            return commands;
        }
        return Collections.emptyList();
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
