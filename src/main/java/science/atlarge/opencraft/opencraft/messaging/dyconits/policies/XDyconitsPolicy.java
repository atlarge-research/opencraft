package science.atlarge.opencraft.opencraft.messaging.dyconits.policies;

import com.flowpowered.network.Message;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import science.atlarge.opencraft.dyconits.Bounds;
import science.atlarge.opencraft.dyconits.Subscriber;
import science.atlarge.opencraft.dyconits.policies.DyconitCommand;
import science.atlarge.opencraft.dyconits.policies.DyconitPolicy;
import science.atlarge.opencraft.dyconits.policies.DyconitSubscribeCommand;

public class XDyconitsPolicy implements DyconitPolicy<Player, Message> {

    private final int num;
    private final int staleness;
    private final int numerical;
    private final Random random = new Random();

    public XDyconitsPolicy(int num, int staleness, int numerical) {
        this.num = num;
        this.staleness = staleness;
        this.numerical = numerical;
    }

    @NotNull
    @Override
    public String computeAffectedDyconit(@NotNull Object o) {
        return String.valueOf(random.nextInt(num));
    }

    @NotNull
    @Override
    public List<DyconitCommand<Player, Message>> update(@NotNull Subscriber<Player, Message> subscriber) {
        return IntStream.range(0, num)
                .mapToObj(i -> new DyconitSubscribeCommand<>(subscriber.getKey(), subscriber.getCallback(), new Bounds(staleness, numerical), String.valueOf(i)))
                .collect(Collectors.toList());
    }

    @Override
    public int weigh(Message message) {
        return 1;
    }
}
