package net.glowstone.dyconit;

/*
Policy 1: Epicenter
 */

import org.bukkit.entity.Player;

class PolicyEpicenter implements IPolicy {
    private static final PolicyEpicenter policy = new PolicyEpicenter();

    private PolicyEpicenter() {}

    static IPolicy setPolicy() { return policy; }

    @Override
    public void enforce(Player p, DyconitCollection dyconits) {
        System.out.println("enforce policy");
    }
}
