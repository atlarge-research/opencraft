package net.glowstone.block.physics;

import net.glowstone.block.BlockID;
import net.glowstone.block.GlowBlockState;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;

public class DoubleStepPhysics extends DefaultBlockPhysics {

    @Override
    public GlowBlockState placeAgainst(GlowBlockState block, int type, short data, BlockFace against) {
        if (against == BlockFace.UP && type == BlockID.STEP) {
            GlowBlockState possibleStair = block.getBlock().getRelative(against.getOppositeFace()).getState();
            if (possibleStair.getTypeId() == BlockID.STEP && possibleStair.getRawData() == data) {
                possibleStair.setTypeId(BlockID.DOUBLE_STEP);
                possibleStair.setData(new MaterialData(BlockID.DOUBLE_STEP, (byte) data));
                return possibleStair;
            }
        }
        return super.placeAgainst(block, type, data, against);
    }
}