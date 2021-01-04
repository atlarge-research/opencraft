package science.atlarge.opencraft.opencraft.messaging.dyconits.policies;

import com.flowpowered.network.Message;
import java.util.Collections;
import java.util.List;
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

public class SingleDyconitPolicy implements DyconitPolicy<Player, Message> {

    private final int staleness;
    private final int numerical;
    private final static String DYCONIT_NAME = "single";

    private final WeighMessage weighMessage;

    public SingleDyconitPolicy(int staleness, int numerical, GlowServer server) {
        this.staleness = staleness;
        this.numerical = numerical;
        this.weighMessage = new DistanceMoved(server, new Bounds(-1, 5));
    }

    @NotNull
    @Override
    public String computeAffectedDyconit(@NotNull Object o) {
        return DYCONIT_NAME;
    }

    @NotNull
    @Override
    public List<DyconitCommand<Player, Message>> update(@NotNull Subscriber<Player, Message> subscriber) {
        return Collections.singletonList(new DyconitSubscribeCommand<>(subscriber.getKey(), subscriber.getCallback(), new Bounds(staleness, numerical), DYCONIT_NAME));
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
