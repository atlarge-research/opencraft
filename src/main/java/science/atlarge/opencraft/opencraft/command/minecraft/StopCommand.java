package science.atlarge.opencraft.opencraft.command.minecraft;

import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.ServerProvider;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

/**
 * In vanilla Minecraft, this command takes no arguments; but as an extension, Glowstone treats any
 * arguments as a custom kick message.
 */
public class StopCommand extends GlowVanillaCommand {

    public StopCommand() {
        super("stop");
        setPermission("minecraft.command.stop"); // NON-NLS
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args,
            CommandMessages commandMessages) {
        if (!testPermission(sender, commandMessages.getPermissionMessage())) {
            return true;
        }
        Server server = ServerProvider.getServer();
        if (args.length > 0 && server instanceof GlowServer) {
            ((GlowServer) server).shutdown(String.join(" ", args));
        } else {
            server.shutdown();
        }
        return true;
    }
}
