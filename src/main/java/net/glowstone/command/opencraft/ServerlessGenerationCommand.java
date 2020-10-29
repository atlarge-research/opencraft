package net.glowstone.command.opencraft;

import net.glowstone.GlowWorld;
import net.glowstone.command.CommandUtils;
import net.glowstone.command.minecraft.GlowVanillaCommand;
import net.glowstone.i18n.LocalizedStringImpl;
import org.bukkit.command.CommandSender;

public class ServerlessGenerationCommand extends GlowVanillaCommand {
    public ServerlessGenerationCommand() {
        super("slgen");
    }

    @Override
    protected boolean execute(CommandSender sender, String commandLabel, String[] args, CommandMessages localizedMessages) {
        GlowWorld world = CommandUtils.getWorld(sender);
        if (args.length < 1) {
            new LocalizedStringImpl("slgen.done", localizedMessages.getResourceBundle()).send(sender, world.getServerlessGenerationLevel());
            return false;
        }

        try {
            world.setServerlessGenerationLevel(Integer.parseInt(args[0]));
            new LocalizedStringImpl("slgen.done", localizedMessages.getResourceBundle()).send(sender, args[0]);
            return true;
        } catch (Exception ex) {
            new LocalizedStringImpl("slgen.usage", localizedMessages.getResourceBundle()).send(sender);
            return false;
        }
    }
}
