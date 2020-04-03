package net.glowstone.dyconit;

import net.glowstone.chunk.GlowChunk;
import org.bukkit.entity.Player;

public class PolicyDefault implements IPolicy {
    private static final PolicyDefault policy = new PolicyDefault();

    private PolicyDefault() {}

    static IPolicy setPolicy() { return policy; }

    @Override
    public void enforce(Player p, DyconitCollection dyconits) {
        for (GlowChunk.Key key : dyconits.getKeyDyconitMap().keySet()) {
            Dyconit.Subscription sub = dyconits.retrieveDyconit(key)
                                                        .subscriptions.get(p);

            if (sub == null) { continue; }

            sub.stalenessBound = 0;
            sub.numericalErrorBound = 0;
        }
    }
}
