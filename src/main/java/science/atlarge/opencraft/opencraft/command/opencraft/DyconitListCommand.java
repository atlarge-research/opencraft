package science.atlarge.opencraft.opencraft.command.opencraft;

import java.util.List;
import java.util.ResourceBundle;
import org.bukkit.command.CommandSender;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.command.minecraft.GlowVanillaCommand;
import science.atlarge.opencraft.opencraft.i18n.LocalizedStringImpl;
import science.atlarge.opencraft.opencraft.messaging.DyconitMessaging;
import science.atlarge.opencraft.opencraft.messaging.Messaging;

public class DyconitListCommand extends GlowVanillaCommand {

    public DyconitListCommand() {
        // TODO give command a good name
        super("dclist");
    }

    @Override
    protected boolean execute(CommandSender sender, String commandLabel, String[] args, CommandMessages localizedMessages) {
        final ResourceBundle resourceBundle = localizedMessages.getResourceBundle();
        Messaging messaging = ((GlowWorld) sender.getServer().getWorlds().get(0)).getMessagingSystem();

        if (messaging instanceof DyconitMessaging) {
            DyconitMessaging dm = (DyconitMessaging) messaging;
            List<String> dyconits = dm.getDyconits();
            new LocalizedStringImpl("dclist.done", resourceBundle).send(sender, String.join("\n", dyconits));
            return true;
        } else {
            // TODO, ERROR
            return false;
        }
    }
}
