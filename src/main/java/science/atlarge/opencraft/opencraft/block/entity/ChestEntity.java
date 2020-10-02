package science.atlarge.opencraft.opencraft.block.entity;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.block.entity.state.GlowChest;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.inventory.GlowChestInventory;
import science.atlarge.opencraft.opencraft.net.message.play.game.BlockActionMessage;
import science.atlarge.opencraft.opencraft.util.SoundUtil;
import org.bukkit.Sound;

/**
 * Block entity for Chests.
 */
public class ChestEntity extends ContainerEntity {

    private int viewers;

    public ChestEntity(GlowBlock block) {
        super(block, new GlowChestInventory(new GlowChest(block)));
        setSaveId("minecraft:chest");
    }

    @Override
    public GlowBlockState getState() {
        return new GlowChest(block);
    }

    /**
     * Increments the count of viewing players, and plays the opening sound if this chest isn't
     * already open.
     */
    public void addViewer() {
        viewers++;
        if (viewers == 1) {
            updateInRange();
            SoundUtil
                .playSoundPitchRange(block.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5F, 0.9F, 0.1F);
        }
    }

    /**
     * Decrements the count of viewing players, and plays the chest closing sound if nobody else is
     * holding the chest open.
     */
    public void removeViewer() {
        viewers--;
        if (viewers == 0) {
            updateInRange();
            SoundUtil.playSoundPitchRange(block.getLocation(), Sound.BLOCK_CHEST_CLOSE, 0.5F, 0.9F,
                0.1F);
        }
    }

    @Override
    public void update(GlowPlayer player) {
        super.update(player);

        player.getSession().send(new BlockActionMessage(block.getX(), block.getY(), block.getZ(), 1,
            viewers == 0 ? 0 : 1, block.getTypeId()));
    }
}
