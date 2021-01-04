package science.atlarge.opencraft.opencraft.messaging.dyconits.policies;

import com.flowpowered.network.Message;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import science.atlarge.opencraft.dyconits.Subscriber;
import science.atlarge.opencraft.dyconits.policies.DyconitChangePolicyCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitPolicy;
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.OverloadBreaker;

public class DonnybrookWhenNeeded implements DyconitPolicy<Player, Message> {

    private final GlowServer server;
    private final OverloadBreaker breaker;

    private DyconitPolicy<Player, Message> policy = new ZeroBoundsPolicy();
    private int numPlayersWhenOverloaded;

    public DonnybrookWhenNeeded(GlowServer server) {
        this.server = server;
        this.breaker = server.getScheduler().getBreaker();
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
        if (breaker.overloaded(5000, 0.9)) {
            numPlayersWhenOverloaded = server.getOnlinePlayers().size();
            GlowServer.eventLogger.log("dcpolicy_stop", policy.getClass().getSimpleName());
            policy = new DonnybrookPolicy(server);
            GlowServer.eventLogger.log("dcpolicy_start", policy.getClass().getSimpleName());
            return Collections.singletonList(new DyconitChangePolicyCommand<>(policy));
        } else if (server.getOnlinePlayers().size() < numPlayersWhenOverloaded - 50) {
            GlowServer.eventLogger.log("dcpolicy_stop", policy.getClass().getSimpleName());
            policy = new SingleDyconitPolicy(0, 0, server);
            GlowServer.eventLogger.log("dcpolicy_start", policy.getClass().getSimpleName());
            return Collections.singletonList(new DyconitChangePolicyCommand<>(policy));
        }
        return Collections.emptyList();
    }
}
