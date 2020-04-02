package net.glowstone.dyconit;

import org.bukkit.entity.Player;

interface IPolicy {
    void enforce(Player p, DyconitCollection dyconits);
}
