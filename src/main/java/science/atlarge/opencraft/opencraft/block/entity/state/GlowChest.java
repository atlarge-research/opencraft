package science.atlarge.opencraft.opencraft.block.entity.state;

import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.ItemTable;
import science.atlarge.opencraft.opencraft.block.blocktype.BlockChest;
import science.atlarge.opencraft.opencraft.block.entity.ChestEntity;
import science.atlarge.opencraft.opencraft.inventory.GlowDoubleChestInventory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GlowChest extends GlowContainer implements Chest {

    public GlowChest(GlowBlock block) {
        super(block);
    }

    public ChestEntity getBlockEntity() {
        return (ChestEntity) getBlock().getBlockEntity();
    }

    @Override
    public Inventory getBlockInventory() {
        return getBlockEntity().getInventory();
    }

    @Override
    public Inventory getInventory() {
        GlowBlock me = getBlock();
        BlockChest blockChest = (BlockChest) ItemTable.instance().getBlock(me.getType());
        BlockFace attachedChest = blockChest.getAttachedChest(me);

        if (attachedChest != null) {
            Block nearbyBlock = me.getRelative(attachedChest);
            GlowChest nearbyChest = (GlowChest) nearbyBlock.getState();

            switch (attachedChest) {
                case SOUTH:
                case EAST:
                    return new GlowDoubleChestInventory(this, nearbyChest);
                case WEST:
                case NORTH:
                    return new GlowDoubleChestInventory(nearbyChest, this);

                default:
                    GlowServer.logger.warning(
                        "GlowChest#getInventory() can only handle N/O/S/W BlockFaces, got "
                            + attachedChest);
                    return getBlockInventory();
            }
        }

        return getBlockInventory();
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        ItemStack[] contents = getBlockInventory().getContents();

        boolean result = super.update(force, applyPhysics);

        if (result) {
            getBlockEntity().setContents(contents);
            getBlockEntity().updateInRange();
        }

        return result;
    }
}
