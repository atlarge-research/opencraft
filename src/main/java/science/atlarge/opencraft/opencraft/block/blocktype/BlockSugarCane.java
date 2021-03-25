package science.atlarge.opencraft.opencraft.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.google.gson.Gson;
import org.bukkit.util.Vector;
import science.atlarge.opencraft.opencraft.EventFactory;
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.inventory.ItemStack;
import science.atlarge.opencraft.opencraft.serverless.BlockSet;
import science.atlarge.opencraft.opencraft.serverless.Invoker;
import science.atlarge.opencraft.opencraft.serverless.SimulatedRegistry;
import science.atlarge.opencraft.opencraft.serverless.StateStore;

public class BlockSugarCane extends BlockNeedsAttached {

    static int count = 0;

    @Override
    public void onNearBlockChanged(GlowBlock block, BlockFace face, GlowBlock changedBlock,
        Material oldType, byte oldData, Material newType, byte newData) {
        updatePhysics(block);
    }

    @Override
    public boolean canPlaceAt(GlowPlayer player, GlowBlock block, BlockFace against) {
        Block below = block.getRelative(BlockFace.DOWN);
        Material type = below.getType();
        switch (type) {
            case SUGAR_CANE_BLOCK:
                return true;
            case DIRT:
            case GRASS:
            case SAND:
                return isNearWater(below);
            default:
                return false;
        }
    }

    @Override
    public boolean canTickRandomly() {
        return true;
    }

    @Override
    public void updateBlock(GlowBlock block) {
        if (!canPlaceAt(null, block, BlockFace.DOWN)) {
            block.breakNaturally();
            return;
        }

        GlowServer.logger.info("Updating block!");

        GlowBlock blockAbove = block.getRelative(BlockFace.UP);
        // check it's the highest block of sugar cane
        if (blockAbove.isEmpty()) {
            // check the current cane height
            Block blockBelow = block.getRelative(BlockFace.DOWN);
            int height = 1;
            while (blockBelow.getType() == Material.SUGAR_CANE_BLOCK) {
                height++;
                blockBelow = blockBelow.getRelative(BlockFace.DOWN);
            }
            if (height < 3) {
                GlowBlockState state = block.getState();
                if (state.getRawData() < 15) {
                    // increase age
                    state.setRawData((byte) (state.getRawData() + 1));
                    state.update(true);
                } else {
                    // grow the sugar cane on the above block
                    state.setRawData((byte) 0);
                    state.update(true);
                    state = blockAbove.getState();
                    state.setType(Material.SUGAR_CANE_BLOCK);
                    state.setRawData((byte) 0);
                    BlockGrowEvent growEvent = new BlockGrowEvent(blockAbove, state);
                    EventFactory.getInstance().callEvent(growEvent);
                    if (!growEvent.isCancelled()) {
                        state.update(true);
                    }
                    updatePhysics(blockAbove);
                }
            }
        }
    }

    private boolean isNearWater(Block block) {
        for (BlockFace face : SIDES) {
            switch (block.getRelative(face).getType()) {
                case WATER:
                case STATIONARY_WATER:
                    return true;
                default:
                    break;
            }
        }

        return false;
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock me, ItemStack tool) {
        // Overridden for sugar cane to remove data from the dropped item
        return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.SUGAR_CANE)));
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        state.update(true);
        GlowServer.logger.info("Placed a sugar cane block!");
        GlowServer.logger.info(String.format("typeId: %s", state.getTypeId()));
        GlowServer.logger.info(String.format("X: %s, Y: %s, Z: %s", state.getX(), state.getY(),state.getZ()));
        BlockSet blockSet = new BlockSet(state.getBlock(), 2);

        Gson gson = new Gson();
        String json = gson.toJson(blockSet);

        //TODO(Javier): check for volume intersections!
        //TODO(Javier): check for pre-compute invalidation up-stack!
        //TODO(Javier): lambda invocation should be async!
        String[] resultingStates = Invoker.getInstance().simulate(json);

        GlowServer.logger.info(String.format("response from lambda: %d", resultingStates.length));

        SimulatedRegistry.getInstance().registerBlockSet(count, blockSet);
        StateStore.getInstance().put(count, resultingStates);

        count++;
    }
}
