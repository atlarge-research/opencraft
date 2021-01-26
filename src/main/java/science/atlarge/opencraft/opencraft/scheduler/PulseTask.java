package science.atlarge.opencraft.opencraft.scheduler;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.ItemTable;
import science.atlarge.opencraft.opencraft.block.blocktype.BlockType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class PulseTask extends BukkitRunnable {

    private final Location location;
    private final Material originalMaterial;
    private boolean async;
    private long delay;
    private boolean single;

    /**
     * Creates a block update task.
     *
     * @param block the block to update
     * @param async whether to run asynchronously
     * @param delay the ticks to wait before running the task
     * @param single if true, run this task only once; if false, repeat every {@code delay} ticks
     */
    public PulseTask(GlowBlock block, boolean async, long delay, boolean single) {
        this.location = block.getLocation();
        this.originalMaterial = block.getType();
        this.async = async;
        this.delay = delay;
        this.single = single;
    }

    /**
     * Schedules this task.
     */
    public void startPulseTask() {
        if (single) {
            if (async) {
                runTaskLaterAsynchronously(null, delay);
            } else {
                runTaskLater(null, delay);
            }
        } else {
            if (async) {
                runTaskTimerAsynchronously(null, delay, delay);
            } else {
                runTaskTimer(null, delay, delay);
            }
        }
    }

    @Override
    public void run() {
        Block block = location.getBlock();
        if (block == null) {
            cancel();
            return;
        }
        if (!location.getChunk().isLoaded()) {
            return;
        }
        if (block.getType() != originalMaterial) {
            cancel();
            return;
        }
        ItemTable table = ItemTable.instance();
        BlockType type = table.getBlock(originalMaterial);
        if (type != null) {
            type.receivePulse((GlowBlock) block);
        }
    }
}
