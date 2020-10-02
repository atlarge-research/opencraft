package science.atlarge.opencraft.opencraft.command.opencraft;

import com.flowpowered.network.Message;
import java.util.ResourceBundle;
import science.atlarge.opencraft.opencraft.command.minecraft.GlowVanillaCommand;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.i18n.LocalizedStringImpl;
import science.atlarge.opencraft.opencraft.messaging.DyconitMessaging;
import science.atlarge.opencraft.opencraft.messaging.Messaging;
import science.atlarge.opencraft.opencraft.messaging.dyconits.policies.ChunkPolicy;
import science.atlarge.opencraft.opencraft.messaging.dyconits.policies.InfiniteBoundsPolicy;
import science.atlarge.opencraft.opencraft.messaging.dyconits.policies.ZeroBoundsPolicy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import science.atlarge.opencraft.dyconits.policies.DyconitPolicy;

public class DyconitPolicyCommand extends GlowVanillaCommand {

    public DyconitPolicyCommand() {
        // TODO give command a good name
        super("dcpolicy");
    }

    @Override
    protected boolean execute(CommandSender sender, String commandLabel, String[] args, CommandMessages localizedMessages) {
        final ResourceBundle resourceBundle = localizedMessages.getResourceBundle();
        GlowPlayer player = (GlowPlayer) sender;
        Messaging messaging = player.getWorld().getMessagingSystem();

        if (messaging instanceof DyconitMessaging) {
            DyconitMessaging dm = (DyconitMessaging) messaging;
            if (args.length > 0) {
                DyconitPolicy<Player, Message> policy = policyFromString(args[0], player);
                if (policy == null) {
                    return false;
                }
                dm.setPolicy(policy);
            }
            new LocalizedStringImpl("dcpolicy.done", resourceBundle).send(sender, dm.getPolicy().getClass().getSimpleName());
            return true;
        } else {
            // TODO, ERROR
            return false;
        }
    }

    private @Nullable DyconitPolicy<Player, Message> policyFromString(String policyName, GlowPlayer player) {
        if (nameMatches(ChunkPolicy.class, policyName)) {
            return new ChunkPolicy(player.getServer().getViewDistance());
        } else if (nameMatches(ZeroBoundsPolicy.class, policyName)) {
            return new ZeroBoundsPolicy();
        } else if (nameMatches(InfiniteBoundsPolicy.class, policyName)) {
            return new InfiniteBoundsPolicy();
        }
        return null;
    }

    private boolean nameMatches(Class<? extends DyconitPolicy<Player, Message>> policy, String possibleName) {
        return policy.getSimpleName().toLowerCase().startsWith(possibleName.toLowerCase());
    }
}
