package science.atlarge.opencraft.opencraft.block.blocktype;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.inventory.ToolType;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlockIronTrapDoor extends BlockDirectDrops {

    private BlockTrapDoor trapDoor;

    public BlockIronTrapDoor() {
        super(Material.IRON_TRAPDOOR, ToolType.PICKAXE);
        trapDoor = new BlockTrapDoor(this);
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face,
                           ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);
        trapDoor.placeBlock(player, state, face, holding, clickedLoc);
    }

    @Override
    public void onRedstoneUpdate(GlowBlock block) {
        trapDoor.onRedstoneUpdate(block);
    }
}
