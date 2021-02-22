package science.atlarge.opencraft.opencraft.command.opencraft;

import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.chunk.policy.ChunkLoadingPolicy;
import science.atlarge.opencraft.opencraft.command.CommandUtils;
import science.atlarge.opencraft.opencraft.command.minecraft.GlowVanillaCommand;
import science.atlarge.opencraft.opencraft.i18n.LocalizedStringImpl;
import org.bukkit.command.CommandSender;

public class ServerlessGenerationCommand extends GlowVanillaCommand {
    public ServerlessGenerationCommand() {
        super("slgen");
    }

    @Override
    protected boolean execute(CommandSender sender, String commandLabel, String[] args, CommandMessages localizedMessages) {
        GlowWorld world = CommandUtils.getWorld(sender);
        if (args.length < 1) {
            new LocalizedStringImpl("slgen.done", localizedMessages.getResourceBundle())
                    .send(sender, world.getChunkLoadingPolicy().getClass().getSimpleName());
            return false;
        }

        try {
            world.setChunkLoadingPolicy(ChunkLoadingPolicy.fromString(world, args[0]));
            new LocalizedStringImpl("slgen.done", localizedMessages.getResourceBundle())
                    .send(sender, world.getChunkLoadingPolicy().getClass().getSimpleName());
            return true;
        } catch (Exception ex) {
            new LocalizedStringImpl("slgen.usage", localizedMessages.getResourceBundle()).send(sender);
            return false;
        }
    }
}
