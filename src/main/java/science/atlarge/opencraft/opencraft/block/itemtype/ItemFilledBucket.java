package science.atlarge.opencraft.opencraft.block.itemtype;

import lombok.Getter;
import science.atlarge.opencraft.opencraft.EventFactory;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.block.ItemTable;
import science.atlarge.opencraft.opencraft.block.blocktype.BlockType;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemFilledBucket extends ItemType {

    @Getter
    private final BlockType liquid;

    public ItemFilledBucket(Material liquid) {
        this.liquid = ItemTable.instance().getBlock(liquid);
        setMaxStackSize(1);
    }

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock against, BlockFace face,
                                ItemStack holding, Vector clickedLoc, EquipmentSlot hand) {
        GlowBlock target = against.getRelative(face);
        BlockType againstBlockType = ItemTable.instance().getBlock(against.getType());

        // only allow placement inside replaceable blocks

        if (againstBlockType.canAbsorb(target, face, holding)) {
            target = against;
        } else if (!target.isEmpty()) {
            BlockType targetType = ItemTable.instance().getBlock(target.getType());
            if (!targetType.canOverride(target, face, holding)) {
                return;
            }
        }

        GlowBlockState newState = target.getState();

        PlayerBucketEmptyEvent event = EventFactory.getInstance().callEvent(
            new PlayerBucketEmptyEvent(player, target, face, holding.getType(), holding));
        if (event.isCancelled()) {
            return;
        }

        liquid.placeBlock(player, newState, face, holding, clickedLoc);

        // perform the block change
        newState.update(true);

        if (player.getGameMode() != GameMode.CREATIVE) {
            holding.setType(Material.BUCKET);
        }
    }
}
