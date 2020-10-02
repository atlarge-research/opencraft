package science.atlarge.opencraft.opencraft.command.minecraft;

import science.atlarge.opencraft.opencraft.command.CommandUtils;
import science.atlarge.opencraft.opencraft.i18n.LocalizedStringImpl;
import org.bukkit.command.CommandSender;

public class SeedCommand extends GlowVanillaCommand {

    public SeedCommand() {
        super("seed");
        setPermission("minecraft.command.seed"); // NON-NLS
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args,
            CommandMessages commandMessages) {
        if (!testPermission(sender, commandMessages.getPermissionMessage())) {
            return true;
        }
        new LocalizedStringImpl("seed.output", commandMessages.getResourceBundle())
                .send(sender, CommandUtils.getWorld(sender).getSeed());
        return true;
    }
}
