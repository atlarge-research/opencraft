package science.atlarge.opencraft.opencraft.command.minecraft;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.state.BlockStateData;
import science.atlarge.opencraft.opencraft.block.state.InvalidBlockStateException;
import science.atlarge.opencraft.opencraft.block.state.StateSerialization;
import science.atlarge.opencraft.opencraft.command.CommandUtils;
import science.atlarge.opencraft.opencraft.constants.ItemIds;
import science.atlarge.opencraft.opencraft.i18n.LocalizedStringImpl;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

/**
 * Fills a region with a block: {@code /fill x1 y1 z1 x2 y2 z2 block [data|state] [mode]}.
 */
public class FillCommand extends GlowVanillaCommand {

    /**
     * The largest number of blocks that one command may fill, as in vanilla.
     */
    static final int MAX_BLOCKS = 32768;

    private static final List<String> MODES =
            Arrays.asList("replace", "destroy", "keep", "hollow", "outline"); // NON-NLS

    /**
     * Creates the instance for this command.
     */
    public FillCommand() {
        super("fill");
        setPermission("minecraft.command.fill"); // NON-NLS
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args,
            CommandMessages messages) {
        if (!testPermission(sender, messages.getPermissionMessage())) {
            return true;
        }
        if (args.length < 7) {
            sendUsageMessage(sender, messages);
            return false;
        }

        String typeName = CommandUtils.toNamespaced(args[6].toLowerCase());
        Material type = ItemIds.getBlock(typeName);
        if (type == null) {
            new LocalizedStringImpl("fill.invalid.type", messages.getResourceBundle())
                    .sendInColor(ChatColor.RED, sender, typeName);
            return false;
        }
        Byte data = args.length > 7 ? parseData(sender, type, args[7]) : Byte.valueOf((byte) 0);
        if (data == null) {
            return false;
        }

        String mode = args.length > 8 ? args[8].toLowerCase() : "replace"; // NON-NLS
        if (!MODES.contains(mode)) {
            new LocalizedStringImpl("fill.invalid.mode", messages.getResourceBundle())
                    .sendInColor(ChatColor.RED, sender, args[8]);
            return false;
        }
        Material replaced = null;
        int replacedData = -1;
        if (args.length > 9) {
            if (!"replace".equals(mode)) { // NON-NLS
                sendUsageMessage(sender, messages);
                return false;
            }
            String replacedName = CommandUtils.toNamespaced(args[9].toLowerCase());
            replaced = ItemIds.getBlock(replacedName);
            if (replaced == null) {
                new LocalizedStringImpl("fill.invalid.type", messages.getResourceBundle())
                        .sendInColor(ChatColor.RED, sender, replacedName);
                return false;
            }
            if (args.length > 10) {
                Byte oldData = parseData(sender, replaced, args[10]);
                if (oldData == null) {
                    return false;
                }
                replacedData = oldData;
            }
        }

        Location first = CommandUtils.getLocation(sender, args[0], args[1], args[2]);
        Location second = CommandUtils.getLocation(sender, args[3], args[4], args[5]);
        int minX = Math.min(first.getBlockX(), second.getBlockX());
        int minY = Math.min(first.getBlockY(), second.getBlockY());
        int minZ = Math.min(first.getBlockZ(), second.getBlockZ());
        int maxX = Math.max(first.getBlockX(), second.getBlockX());
        int maxY = Math.max(first.getBlockY(), second.getBlockY());
        int maxZ = Math.max(first.getBlockZ(), second.getBlockZ());

        long volume = (long) (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
        if (volume > MAX_BLOCKS) {
            new LocalizedStringImpl("fill.too-many", messages.getResourceBundle())
                    .sendInColor(ChatColor.RED, sender, MAX_BLOCKS, volume);
            return false;
        }

        GlowWorld world = (GlowWorld) first.getWorld();
        int filled = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    boolean shell = isOnShell(x, y, z, minX, minY, minZ, maxX, maxY, maxZ);
                    GlowBlock block = (GlowBlock) world.getBlockAt(x, y, z);
                    if (fill(block, type, data, mode, shell, replaced, replacedData)) {
                        filled++;
                    }
                }
            }
        }

        if (filled == 0) {
            new LocalizedStringImpl("fill.failed", messages.getResourceBundle())
                    .sendInColor(ChatColor.RED, sender);
            return false;
        }
        new LocalizedStringImpl(filled == 1 ? "fill.done.singular" : "fill.done",
                messages.getResourceBundle()).send(sender, filled);
        return true;
    }

    private static boolean fill(GlowBlock block, Material type, byte data, String mode,
            boolean shell, Material replaced, int replacedData) {
        switch (mode) {
            case "keep": // NON-NLS
                if (block.getType() != Material.AIR) {
                    return false;
                }
                break;
            case "outline": // NON-NLS
                if (!shell) {
                    return false;
                }
                break;
            case "hollow": // NON-NLS
                if (!shell) {
                    // Clear the inside.
                    return setBlock(block, Material.AIR, (byte) 0);
                }
                break;
            case "destroy": // NON-NLS
                block.breakNaturally();
                break;
            default:
                if (replaced != null && (block.getType() != replaced
                        || (replacedData >= 0 && block.getData() != replacedData))) {
                    return false;
                }
                break;
        }
        return setBlock(block, type, data);
    }

    private static boolean setBlock(GlowBlock block, Material type, byte data) {
        block.setType(type, data, true);
        return true;
    }

    /**
     * Checks whether a position is on the outer layer of the region.
     */
    static boolean isOnShell(int x, int y, int z, int minX, int minY, int minZ, int maxX,
            int maxY, int maxZ) {
        return x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ;
    }

    private static Byte parseData(CommandSender sender, Material type, String state) {
        BlockStateData data = CommandUtils.readState(sender, type, state);
        if (data == null) {
            return null;
        }
        if (data.isNumeric()) {
            return data.getNumericValue();
        }
        try {
            return StateSerialization.parseData(type, data).getData();
        } catch (InvalidBlockStateException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args)
            throws IllegalArgumentException {
        if (args.length == 7) {
            return ItemIds.getTabCompletion(args[6]);
        }
        if (args.length == 9) {
            return MODES;
        }
        return Collections.emptyList();
    }
}
