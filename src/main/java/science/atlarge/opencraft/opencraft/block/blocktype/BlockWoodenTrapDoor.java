package science.atlarge.opencraft.opencraft.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlockWoodenTrapDoor extends BlockOpenable {

    private static final Collection<ItemStack> DROP = Collections
        .unmodifiableCollection(Arrays.asList(new ItemStack(Material.TRAP_DOOR)));
    private BlockTrapDoor trapDoor;

    public BlockWoodenTrapDoor() {
        trapDoor = new BlockTrapDoor(this);
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face,
        ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);
        trapDoor.placeBlock(player, state, face, holding, clickedLoc);
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
        return DROP;
    }

    @Override
    public void onRedstoneUpdate(GlowBlock block) {
        trapDoor.onRedstoneUpdate(block);
    }
}
