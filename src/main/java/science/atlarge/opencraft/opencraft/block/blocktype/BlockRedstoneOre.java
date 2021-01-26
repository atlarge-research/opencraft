package science.atlarge.opencraft.opencraft.block.blocktype;

import science.atlarge.opencraft.opencraft.EventFactory;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.inventory.ToolType;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.util.Vector;

public class BlockRedstoneOre extends BlockRandomDrops {

    public BlockRedstoneOre() {
        super(Material.REDSTONE, 0, 3, 4, ToolType.IRON_PICKAXE);
    }

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face,
                                 Vector clickedLoc) {
        EntityChangeBlockEvent changeBlockEvent = new EntityChangeBlockEvent(player, block,
            Material.GLOWING_REDSTONE_ORE, (byte) 0);
        EventFactory.getInstance().callEvent(changeBlockEvent);
        if (!changeBlockEvent.isCancelled()) {
            GlowBlockState state = block.getState();
            state.setType(Material.GLOWING_REDSTONE_ORE);
            state.update(true);
        }
        return false;
    }
}
