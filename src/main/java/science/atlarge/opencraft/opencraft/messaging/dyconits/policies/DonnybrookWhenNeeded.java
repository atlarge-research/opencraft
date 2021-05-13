package science.atlarge.opencraft.opencraft.messaging.dyconits.policies;

import com.flowpowered.network.Message;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import science.atlarge.opencraft.dyconits.Subscriber;
import science.atlarge.opencraft.dyconits.policies.DyconitClearCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitPolicy;
import science.atlarge.opencraft.opencraft.EventFactory;
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.OverloadBreaker;

public class DonnybrookWhenNeeded implements DyconitPolicy<Player, Message> {

    private final GlowServer server;
    private final OverloadBreaker breaker;

    private DyconitPolicy<Player, Message> policy;
    private int numPlayersWhenOverloaded;

    public DonnybrookWhenNeeded(GlowServer server) {
        this.server = server;
        this.breaker = server.getScheduler().getBreaker();
        this.policy = new SingleDyconitPolicy(0, 0, server);
    }

    @NotNull
    @Override
    public String computeAffectedDyconit(@NotNull Object o) {
        return policy.computeAffectedDyconit(o);
    }

    @NotNull
    @Override
    public List<DyconitCommand<Player, Message>> update(@NotNull Subscriber<Player, Message> subscriber) {
        return policy.update(subscriber);
    }

    @Override
    public int weigh(Message message) {
        return policy.weigh(message);
    }

    @NotNull
    @Override
    public List<DyconitCommand<Player, Message>> globalUpdate() {
        if (policy instanceof SingleDyconitPolicy && breaker.overloaded(5000, 0.9)) {
            numPlayersWhenOverloaded = EventFactory.getNumPlayers().get();
            changePolicy(new DonnybrookPolicy(server));
            return Collections.singletonList(new DyconitClearCommand());
        } else if (policy instanceof DonnybrookPolicy && EventFactory.getNumPlayers().get() < numPlayersWhenOverloaded) {
            changePolicy(new SingleDyconitPolicy(0, 0, server));
            return Collections.singletonList(new DyconitClearCommand());
        }
        return Collections.emptyList();
    }

    private void changePolicy(DyconitPolicy<Player, Message> policy) {
        GlowServer.eventLogger.log("dcpolicy_stop", this.policy.getClass().getSimpleName());
        this.policy = policy;
        GlowServer.eventLogger.log("dcpolicy_start", this.policy.getClass().getSimpleName());
    }
}
