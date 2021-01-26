package science.atlarge.opencraft.opencraft.block.blocktype;

import com.google.common.collect.ImmutableList;
import science.atlarge.opencraft.opencraft.EventFactory;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.entity.objects.GlowLeashHitch;
import science.atlarge.opencraft.opencraft.inventory.MaterialMatcher;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LeashHitch;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.util.Vector;

public class BlockFence extends BlockDirectDrops {

    public BlockFence(Material dropType, MaterialMatcher neededTool) {
        super(dropType, neededTool);
    }

    public BlockFence(Material dropType) {
        super(dropType);
    }

    @Override
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face,
            Vector clickedLoc) {
        super.blockInteract(player, block, face, clickedLoc);

        if (!player.getLeashedEntities().isEmpty()) {
            LeashHitch leashHitch = GlowLeashHitch.getLeashHitchAt(block);

            ImmutableList.copyOf(player.getLeashedEntities()).stream()
                    .filter(e -> !(EventFactory.getInstance()
                            .callEvent(new PlayerLeashEntityEvent(e, leashHitch, player))
                            .isCancelled()))
                    .forEach(e -> e.setLeashHolder(leashHitch));
            return true;
        }
        return false;
    }
}
