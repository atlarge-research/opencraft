package science.atlarge.opencraft.opencraft.block.blocktype;

import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.inventory.ToolType;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Pumpkin;
import org.bukkit.util.Vector;

public class BlockPumpkinBase extends BlockDirectDrops {

    public BlockPumpkinBase(Material material) {
        super(material, ToolType.AXE);
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face,
        ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);
        MaterialData data = state.getData();
        if (data instanceof Pumpkin) {
            ((Pumpkin) data).setFacingDirection(player.getCardinalFacing());
            state.setData(data);
        } else {
            warnMaterialData(Pumpkin.class, data);
        }
    }
}
