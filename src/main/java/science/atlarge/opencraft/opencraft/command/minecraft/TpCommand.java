package science.atlarge.opencraft.opencraft.command.minecraft;

import science.atlarge.opencraft.opencraft.command.CommandTarget;
import science.atlarge.opencraft.opencraft.command.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Differs from {@link TeleportCommand} in that all relative coordinates are relative to the target
 * rather than the sender. /teleport and /tp were synonyms until Minecraft 1.10.
 */
public class TpCommand extends GlowVanillaCommand {

    /**
     * Creates the instance for this command.
     */
    public TpCommand() {
        super("tp");
        setPermission("minecraft.command.tp"); // NON-NLS
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args,
            CommandMessages commandMessages) {
        if (!testPermission(sender, commandMessages.getPermissionMessage())) {
            return true;
        }
        switch (args.length) {
            case 0:
                sendUsageMessage(sender, commandMessages);
                return false;
            case 1:
                Entity from;
                if (sender instanceof Player) {
                    from = (Entity) sender;
                } else {
                    sender.sendMessage(ChatColor.RED + "Only entities can be teleported");
                    return false;
                }
                String name = args[0];
                if (name.startsWith("@") && !name.startsWith("@e") && name.length() >= 2
                        && CommandUtils.isPhysical(sender)) {
                    Location location = from.getLocation();
                    CommandTarget target = new CommandTarget(sender, name);
                    Entity[] matched = target.getMatched(location);
                    if (matched.length == 0) {
                        commandMessages.getGeneric(GenericMessage.NO_MATCHES)
                                .sendInColor(ChatColor.RED, sender, name);
                        return false;
                    }
                    for (Entity entity : matched) {
                        from.teleport(entity);
                        sender.sendMessage(
                                "Teleported " + CommandUtils.getName(from) + " to " + CommandUtils
                                        .getName(entity));
                    }
                    return true;
                } else {
                    Player player = Bukkit.getPlayerExact(name);
                    if (player == null) {
                        commandMessages.getGeneric(GenericMessage.NO_SUCH_PLAYER)
                                .sendInColor(ChatColor.RED, sender, name);
                        return false;
                    } else {
                        from.teleport(player);
                        sender.sendMessage("Teleported " + CommandUtils.getName(from) + " to "
                                        + player.getName());
                        return true;
                    }
                }
            case 2:
                Entity destination;
                String fromName = args[0];
                String destName = args[1];
                if (fromName.startsWith("@") && fromName.length() >= 2 && CommandUtils
                        .isPhysical(sender)) {
                    Location location = CommandUtils.getLocation(sender);
                    CommandTarget target = new CommandTarget(sender, fromName);
                    Entity[] matched = target.getMatched(location);
                    if (matched.length == 0) {
                        commandMessages.getGeneric(GenericMessage.NO_MATCHES)
                                .sendInColor(ChatColor.RED, sender, fromName);
                        return false;
                    }
                    for (Entity entity : matched) {
                        if (destName.startsWith("@") && !destName.startsWith("@e")
                                && destName.length() >= 2 && CommandUtils.isPhysical(sender)) {
                            Location location2 = CommandUtils.getLocation(sender);
                            CommandTarget target2 = new CommandTarget(sender, destName);
                            Entity[] matched2 = target2.getMatched(location2);
                            if (matched2.length == 0) {
                                commandMessages.getGeneric(GenericMessage.NO_MATCHES)
                                        .sendInColor(ChatColor.RED, sender,
                                        destName);
                                return false;
                            }
                            destination = matched2[0];
                        } else {
                            Player player = Bukkit.getPlayerExact(destName);
                            if (player == null) {
                                commandMessages.getGeneric(GenericMessage.NO_MATCHES)
                                        .sendInColor(ChatColor.RED, sender,
                                        destName);
                                return false;
                            } else {
                                destination = player;
                            }
                        }
                        entity.teleport(destination);
                        sender.sendMessage(
                                "Teleported " + CommandUtils.getName(entity) + " to " + CommandUtils
                                        .getName(destination));
                    }
                    return true;
                } else {
                    Player player = Bukkit.getPlayerExact(fromName);
                    if (player == null) {
                        sender
                                .sendMessage(
                                        ChatColor.RED + "Player '" + fromName + "' is not online");
                        return false;
                    } else {
                        if (destName.startsWith("@") && !destName.startsWith("@e")
                                && destName.length() >= 2 && CommandUtils.isPhysical(sender)) {
                            Location location2 = CommandUtils.getLocation(sender);
                            CommandTarget target2 = new CommandTarget(sender, destName);
                            Entity[] matched2 = target2.getMatched(location2);
                            if (matched2.length == 0) {
                                commandMessages.getGeneric(GenericMessage.NO_MATCHES)
                                        .sendInColor(ChatColor.RED, sender,
                                        destName);
                                return false;
                            }
                            destination = matched2[0];
                        } else {
                            Player player2 = Bukkit.getPlayerExact(destName);
                            if (player2 == null) {
                                commandMessages.getGeneric(GenericMessage.NO_SUCH_PLAYER)
                                        .sendInColor(ChatColor.RED, sender,
                                        destName);
                                return false;
                            } else {
                                destination = player2;
                            }
                        }
                    }
                    player.teleport(destination);
                    sender.sendMessage("Teleported " + player.getName() + " to " + CommandUtils
                            .getName(destination));
                    return true;
                }
            case 3:
            case 4:
            case 5:
            case 6:
                return teleportToCoordinates(sender, args, commandMessages);
            default:
                sendUsageMessage(sender, commandMessages);
                return false;
        }
    }

    /**
     * Teleports to coordinates, with the syntax {@code [target] x y z [yaw pitch]}. Relative
     * coordinates are relative to the target.
     */
    private boolean teleportToCoordinates(CommandSender sender, String[] args,
            CommandMessages commandMessages) {
        // With 3 or 5 arguments the target is omitted and defaults to the sender.
        boolean hasTarget = args.length == 4 || args.length == 6;
        int offset = hasTarget ? 1 : 0;

        Entity target;
        if (hasTarget) {
            String name = args[0];
            if (name.startsWith("@") && name.length() >= 2 && CommandUtils.isPhysical(sender)) {
                Entity[] matched = new CommandTarget(sender, name)
                        .getMatched(CommandUtils.getLocation(sender));
                if (matched.length == 0) {
                    commandMessages.getGeneric(GenericMessage.NO_MATCHES)
                            .sendInColor(ChatColor.RED, sender, name);
                    return false;
                }
                target = matched[0];
            } else {
                target = Bukkit.getPlayerExact(name);
                if (target == null) {
                    commandMessages.getGeneric(GenericMessage.NO_SUCH_PLAYER)
                            .sendInColor(ChatColor.RED, sender, name);
                    return false;
                }
            }
        } else if (sender instanceof Entity) {
            target = (Entity) sender;
        } else {
            sender.sendMessage(ChatColor.RED + "Only entities can be teleported");
            return false;
        }

        Location destination;
        try {
            destination = CommandUtils.getLocation(target.getLocation(), args[offset],
                    args[offset + 1], args[offset + 2]);
            destination.setYaw(target.getLocation().getYaw());
            destination.setPitch(target.getLocation().getPitch());
            if (args.length - offset == 5) {
                destination = CommandUtils.getRotation(destination, args[offset + 3],
                        args[offset + 4]);
            }
        } catch (NumberFormatException e) {
            sendUsageMessage(sender, commandMessages);
            return false;
        }

        target.teleport(destination);
        sender.sendMessage("Teleported " + CommandUtils.getName(target) + " to "
                + destination.getX() + ", " + destination.getY() + ", " + destination.getZ());
        return true;
    }
}
